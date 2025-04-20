package ru.javaboys.vibejson.view.vibejsonchat;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.codeeditor.JmixCodeEditor;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import ru.javaboys.vibejson.entity.ChatMessage;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.JsonDslSchema;
import ru.javaboys.vibejson.entity.SenderType;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.llm.service.LLMService;
import ru.javaboys.vibejson.view.login.LoginView;
import ru.javaboys.vibejson.view.main.MainView;

@Route(value = "vibe-json-chat", layout = MainView.class)
@ViewController(id = "VibeJsonChatView")
@ViewDescriptor(path = "vibe-json-chat.xml")
@CssImport("./styles/waiting-spinner.css")
public class VibeJsonChatView extends StandardView {
    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    private static final String FIND_LATEST_SCHEMA_BY_CONVERSATION_SQL = """
            select m.jsonDslSchema.schemaText
                from ChatMessage m
                where m.conversation.id = :conversationId
                  and m.jsonDslSchema is not null
                  and m.createdDate = (
                    select max(m2.createdDate)
                    from ChatMessage m2
                    where m2.conversation.id = :conversationId
                      and m2.jsonDslSchema is not null
                  )
            """;

    private Conversation currentConversation;

    @Autowired
    private Map<String, LLMService> llmServiceMap;

    @ViewComponent
    private JmixCodeEditor jsonTextArea;

    @ViewComponent
    private JmixComboBox<LLMService> llmComboBox;

    @ViewComponent
    private DataGrid<Conversation> conversationsDataGrid;

    @ViewComponent
    private CollectionContainer<Conversation> conversationDc;

    @ViewComponent("chatMessagesDl")
    private CollectionLoader<ChatMessage> chatMessagesDl;

    @Autowired
    private Metadata metadata;

    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private CollectionLoader<Conversation> conversationDl;

    @ViewComponent
    private JmixTextArea chatTextArea;

    @ViewComponent
    private JmixTextArea promptInput;

    @ViewComponent
    private CollectionContainer<ChatMessage> chatMessagesDc;

    @ViewComponent
    private JmixButton sendButton;

    @Autowired
    private Notifications notifications;

    @Autowired
    private SystemAuthenticator systemAuthenticator;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    private final ObjectMapper mapper = new ObjectMapper();

    private final ExecutorService asyncPool = Executors.newFixedThreadPool(4);

    @Subscribe
    public void onInit(final InitEvent event) {
        llmComboBox.setItems(llmServiceMap.values());
        llmComboBox.setItemLabelGenerator(LLMService::getModelCode);

        // default
        llmComboBox.setValue(llmServiceMap.get("lLMServiceDemo"));

        getContent().getElement().executeJs(
                String.format("""
                        const textarea = this.querySelector('[id$=\\"%s\\"]');\
                        if (!textarea) return;\
                        textarea.addEventListener('keydown', function(e) {\
                          if (e.key === 'Enter' && e.ctrlKey) {\
                            e.preventDefault();\
                            textarea.value += '\\n';\
                          } else if (e.key === 'Enter') {\
                            e.preventDefault();\
                            this.$server.sendPrompt(textarea.value);\
                            textarea.value = "";\
                          }\
                        }.bind(this));""", promptInput.getId().orElse(null))
        );

    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        conversationDl.load();

        // Для JSON‑области
        jsonTextArea.getElement().executeJs(
                "$0.querySelector('textarea').setAttribute('disabled', 'true')"
        );

        // Для окна чата
        chatTextArea.getElement().executeJs(
                "$0.querySelector('textarea').setAttribute('disabled', 'true')"
        );
    }

    @Subscribe(id = "createConversation", subject = "clickListener")
    public void onCreateConversationClick(final ClickEvent<JmixButton> event) {
        // создаём и сохраняем новую беседу
        Conversation conversation = metadata.create(Conversation.class);
        conversation.setTitle("New Chat");
        dataManager.save(conversation);

        // добавляем в начало списка и выбираем
        conversationDc.getMutableItems().add(0, conversation);
        currentConversation = conversation;

        // очищаем историю и JSON‑панель
        resetChatArea();

        conversationsDataGrid.select(conversation);
    }

    @Subscribe("conversationsDataGrid")
    public void onConversationsDataGridItemClick(final ItemClickEvent<Conversation> event) {
        // текущая выбранная беседа
        currentConversation = event.getItem();

        //  Когда conversation не выбран: очищаем чат и JSON‑панель и выходим
        if (currentConversation == null) {
            clearChatPane();

            return;
        }

        // Загружаем все сообщения текущего разговора
        loadChatHistory(currentConversation);

        // Подгружаем «самую свежую» JSON‑схему, если она есть
        loadLatestWorkflow(currentConversation);
    }

    @Subscribe(id = "sendButton", subject = "clickListener")
    public void onSendButtonClick(final ClickEvent<JmixButton> event) {
        // Считываем текст промпта пользователя
        String prompt = promptInput.getValue();
        if (prompt == null || prompt.isBlank()) {
            warn("Пустое сообщение");
            return;
        }

        sendPrompt(prompt);
    }

    @ClientCallable
    private void sendPrompt(String prompt) {
        // Берём выбранную модель из ComboBox
        LLMService llmService = llmComboBox.getValue();
        if (llmService == null) {
            warn("Выберите модель");
            return;
        }

        // Получаем (или создаём) беседу
        Conversation conversation = ensureConversation();

        // Сохраняем сообщение пользователя и показываем в чате
        ChatMessage userMsg = saveUserMessage(conversation, prompt);
        appendToChat(userMsg);

        // блокируем ввод пока ждём LLM
        sendButton.setEnabled(false);
        promptInput.setEnabled(false);

        callLlmAsync(llmService, conversation, prompt);
    }

    @Subscribe(id = "copyJsonButton", subject = "clickListener")
    public void onCopyJsonButtonClick(final ClickEvent<JmixButton> event) {
        String json = jsonTextArea.getValue();
        if (json == null || json.isBlank()) {
            Notification.show("Нечего копировать");
            return;
        }

        getUI().ifPresent(ui ->
                ui.getPage().executeJs(
                        "navigator.clipboard.writeText($0)"
                        , json
                )
        );
        Notification.show("JSON скопирован");
    }

    private String formatJson(String rawJson) {
        try {
            Object json = mapper.readValue(rawJson, Object.class);
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
        } catch (Exception e) {
            return rawJson;
        }
    }

    private String formatChatMessage(ChatMessage msg) {
        String who = msg.getSenderType().name();
        String time = msg.getCreatedDate()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return String.format("[%s] %s: %s", time, who, msg.getContent());
    }

    private void appendToChat(ChatMessage msg) {
        String formatted = formatChatMessage(msg);

        String existing = chatTextArea.getValue();
        if (existing == null || existing.isBlank()) {
            chatTextArea.setValue(formatted);
        } else {
            chatTextArea.setValue(existing + "\n\n" + formatted);
        }

        chatTextArea.scrollToEnd();
    }

    private void resetChatArea() {
        chatMessagesDl.removeParameter("conversation");
        chatMessagesDl.getContainer().getMutableItems().clear();

        chatTextArea.clear();
        jsonTextArea.clear();
    }

    private void loadChatHistory(Conversation conversation) {
        chatMessagesDl.setParameter("conversation", conversation);
        chatMessagesDl.load();

        String history = chatMessagesDc.getItems().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedDate))
                .map(this::formatChatMessage)
                .collect(Collectors.joining("\n\n"));

        chatTextArea.setValue(history);
        chatTextArea.scrollToEnd();
    }

    private void loadLatestWorkflow(Conversation conversation) {
        Optional<String> jsonOpt = dataManager.loadValue(
                        FIND_LATEST_SCHEMA_BY_CONVERSATION_SQL, String.class)
                .parameter("conversationId", conversation.getId())
                .optional();

        if (jsonOpt.isPresent()) {
            jsonTextArea.setValue(formatJson(jsonOpt.get()));
            jsonTextArea.scrollIntoView();
        } else {
            jsonTextArea.clear();
        }
    }

    private void clearChatPane() {
        chatTextArea.clear();
        jsonTextArea.clear();
    }

    private Conversation ensureConversation() {
        if (currentConversation != null) return currentConversation;

        Conversation conversation = metadata.create(Conversation.class);
        conversation.setTitle("New Chat");
        dataManager.save(conversation);

        conversationDc.getMutableItems().add(0, conversation);
        conversationsDataGrid.select(conversation);
        currentConversation = conversation;

        return conversation;
    }

    private void warn(String text) {
        notifications.create(text)
                .withType(Notifications.Type.WARNING)
                .withDuration(3_000)
                .show();
    }

    private ChatMessage saveUserMessage(Conversation conversation, String prompt) {
        ChatMessage message = metadata.create(ChatMessage.class);

        message.setConversation(conversation);
        message.setSenderType(SenderType.USER);
        message.setContent(prompt);
        dataManager.save(message);
        chatMessagesDc.getMutableItems().add(message);

        return message;
    }

    private void processLlmResult(LLMResponseDto dto, Throwable exception) {
        sendButton.setEnabled(true);
        promptInput.setEnabled(true);

        if (exception != null) {
            notifications.create("Ошибка LLM: " + exception.getMessage())
                    .withType(Notifications.Type.ERROR)
                    .show();
            return;
        }

        // сохраняем сообщение бота
        ChatMessage botMsg = metadata.create(ChatMessage.class);
        botMsg.setConversation(currentConversation);
        botMsg.setSenderType(SenderType.BOT);
        botMsg.setContent(dto.getLLMChatMsg());

        // если пришёл workflow‑JSON, сохраняем его и показываем
        String workflowJson = dto.getWorkflow();
        if (workflowJson != null && !workflowJson.isBlank()) {
            JsonDslSchema schema = metadata.create(JsonDslSchema.class);
            schema.setSchemaText(workflowJson);
            botMsg.setJsonDslSchema(schema);

            jsonTextArea.setValue(workflowJson);
            jsonTextArea.scrollIntoView();
        }

        // окончательное сохранение сообщения и обновление UI
        dataManager.save(botMsg);
        chatMessagesDc.getMutableItems().add(botMsg);
        appendToChat(botMsg);
        chatTextArea.scrollToEnd();

        // очищаем поле ввода
        promptInput.clear();
    }

    private void callLlmAsync(LLMService llmService,
                              Conversation conversation,
                              String prompt) {

        Authentication auth = currentAuthentication.getAuthentication();

        UI ui = UI.getCurrent();

        String username = auth.getName();

        CompletableFuture
                .supplyAsync(
                        () -> {
                            long startedAt = System.currentTimeMillis();
                            log.info("---> Prompt process stated; conversationId = {}", conversation.getId());
                            LLMResponseDto dto = systemAuthenticator.withUser(username,
                                    () -> llmService.userPromptToWorkflow(conversation, prompt));
                            log.info("---> Prompt process finished(took {} ms); conversationId = {}",
                                    System.currentTimeMillis() - startedAt,
                                    conversation.getId());
                            return dto;
                        },
                        asyncPool
                )
                .whenComplete((dto, ex) ->
                        ui.access(() -> processLlmResult(dto, ex))
                );
    }
}