package ru.javaboys.vibejson.view.vibejsonchat;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaboys.vibejson.entity.ChatMessage;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.SenderType;
import ru.javaboys.vibejson.view.main.MainView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Route(value = "vibe-json-chat", layout = MainView.class)
@ViewController(id = "VibeJsonChat")
@ViewDescriptor(path = "vibe-json-chat.xml")
public class VibeJsonChat extends StandardView {
    private static final List<String> BOT_RESPONSES = List.of(
            "Sure, I'll take a look!",
            "Let me think...",
            "I'm not sure, sorry.",
            "Here's what I found for you."
    );

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

    @ViewComponent
    private JmixTextArea jsonTextArea;

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


    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        conversationDl.load();
    }

    @Subscribe(id = "createConversation", subject = "clickListener")
    public void onCreateConversationClick(final ClickEvent<JmixButton> event) {
        Conversation conversation = metadata.create(Conversation.class);
        conversation.setTitle("New Chat");
        dataManager.save(conversation);

        conversationDc.getMutableItems().add(0, conversation);
        currentConversation = conversation;

        conversationsDataGrid.select(conversation);
    }

    @Subscribe("conversationsDataGrid")
    public void onConversationsDataGridItemClick(final ItemClickEvent<Conversation> event) {
        currentConversation = event.getItem();

        // Если ничего не выбрано — очищаем области
        if (currentConversation == null) {
            chatTextArea.clear();
            jsonTextArea.clear();
            return;
        }

        // 1) Загружаем все сообщения для этого разговора
        chatMessagesDl.setParameter("conversation", currentConversation);
        chatMessagesDl.load();

        // 2) Формируем историю переписки
        String history = chatMessagesDc.getItems().stream()
                .map(this::formatChatMessage)
                .collect(Collectors.joining("\n\n"));

        chatTextArea.setValue(history);
        chatTextArea.scrollToEnd();

        // 3) Подгружаем самый свежий JSON‑схему через JPQL‑запрос
        Optional<String> latestJson = dataManager.loadValue(
                        FIND_LATEST_SCHEMA_BY_CONVERSATION_SQL, String.class)
                .parameter("conversationId", currentConversation.getId())
                .optional();

        if (latestJson.isPresent()) {
            jsonTextArea.setValue(latestJson.get());
            jsonTextArea.scrollToStart();
        } else {
            jsonTextArea.clear();
        }
    }

    @Subscribe(id = "sendButton", subject = "clickListener")
    public void onSendButtonClick(final ClickEvent<JmixButton> event) {
        String message = promptInput.getValue();
        if (currentConversation == null || message == null || message.isBlank()) {
            return;
        }

        // блокируем кнопку
        sendButton.setEnabled(false);

        // 1) Сохраняем сообщение от пользователя
        ChatMessage userMsg = metadata.create(ChatMessage.class);
        userMsg.setConversation(currentConversation);
        userMsg.setSenderType(SenderType.USER);
        userMsg.setContent(message);
        dataManager.save(userMsg);
        chatMessagesDc.getMutableItems().add(userMsg);

        appendToChat(userMsg);

        // TODO: здесь будет взаимодействие с чатом GPT
        ChatMessage botMsg = metadata.create(ChatMessage.class);
        botMsg.setConversation(currentConversation);
        botMsg.setSenderType(SenderType.BOT);
        botMsg.setContent(
                BOT_RESPONSES.get(ThreadLocalRandom.current()
                        .nextInt(BOT_RESPONSES.size()))
        );
        dataManager.save(botMsg);
        chatMessagesDc.getMutableItems().add(botMsg);

        appendToChat(botMsg);

        // 3) Автоскролл вниз
        chatTextArea.scrollToEnd();

        // 4) Очистка поля ввода
        promptInput.clear();

        // разблокируем кнопку
        sendButton.setEnabled(true);
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
}