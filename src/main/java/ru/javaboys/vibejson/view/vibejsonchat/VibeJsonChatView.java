package ru.javaboys.vibejson.view.vibejsonchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.FetchPlan;
import io.jmix.core.Metadata;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.codeeditor.CodeEditorMode;
import io.jmix.flowui.kit.component.codeeditor.CodeEditorTheme;
import io.jmix.flowui.kit.component.codeeditor.JmixCodeEditor;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import ru.javaboys.vibejson.config.AsyncConfiguration;
import ru.javaboys.vibejson.entity.ChatMessage;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.JsonDslSchema;
import ru.javaboys.vibejson.entity.SenderType;
import ru.javaboys.vibejson.entity.User;
import ru.javaboys.vibejson.llm.LLMService;
import ru.javaboys.vibejson.llm.dto.ChatMessageAndWorkflow;
import ru.javaboys.vibejson.view.component.MarkdownRenderer;
import ru.javaboys.vibejson.view.component.WorkflowDiagram;
import ru.javaboys.vibejson.view.main.MainView;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Route(value = "vibe-json-chat", layout = MainView.class)
@ViewController(id = "VibeJsonChatView")
@ViewDescriptor(path = "vibe-json-chat.xml")
public class VibeJsonChatView extends StandardView {

    private static final Logger log = LoggerFactory.getLogger(VibeJsonChatView.class);

    private static final String DEFAULT_TITLE = "Новый чат";
    /** Названия, которые считаются незаданными: беседу с таким именем можно переименовать по первому сообщению. */
    private static final Set<String> DEFAULT_TITLES = Set.of(DEFAULT_TITLE, "New Chat");
    private static final String DEFAULT_MODEL = "SpringAI-OpenAI";
    private static final int TITLE_MAX_LENGTH = 60;

    private static final Locale RU = Locale.forLanguageTag("ru");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("d MMM", RU);
    private static final DateTimeFormatter DAY_TIME = DateTimeFormatter.ofPattern("d MMM, HH:mm", RU);

    private static final List<Suggestion> SUGGESTIONS = List.of(
            new Suggestion(VaadinIcon.CLOCK, "По расписанию",
                    "Каждые 15 минут забирать новые заказы из REST API https://shop.example.com/api/orders "
                            + "и отправлять их в Kafka-топик orders"),
            new Suggestion(VaadinIcon.DATABASE, "Обогащение данных",
                    "При получении сообщения из очереди RabbitMQ clients.in найти клиента в базе данных "
                            + "по email и отправить обогащённые данные в SAP"),
            new Suggestion(VaadinIcon.SPLIT, "Ветвление по условию",
                    "Принять REST-запрос с заявкой, проверить её статус и, если он approved, вызвать процесс "
                            + "выдачи, иначе отправить уведомление в RabbitMQ"),
            new Suggestion(VaadinIcon.FILE_CODE, "Преобразование",
                    "Получать XML из Kafka-топика invoices, преобразовывать его XSLT-шаблоном в формат банка "
                            + "и отправлять в очередь RabbitMQ bank.out")
    );

    private record Suggestion(VaadinIcon icon, String title, String prompt) {
    }

    /** Ответ ассистента, уже сохранённый в базе. */
    private record Reply(ChatMessage message, JsonDslSchema schema) {
    }

    private enum WorkflowTab {DIAGRAM, JSON}

    @Autowired
    private Map<String, LLMService> llmServiceMap;
    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private Notifications notifications;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    @Qualifier(AsyncConfiguration.LLM_EXECUTOR)
    private TaskExecutor llmExecutor;

    @ViewComponent
    private HorizontalLayout chatShell;
    @ViewComponent
    private SplitLayout workSplit;
    @ViewComponent
    private TextField conversationSearch;
    @ViewComponent
    private Div conversationList;
    @ViewComponent
    private Span chatTitle;
    @ViewComponent
    private Span chatSubtitle;
    @ViewComponent
    private JmixComboBox<LLMService> llmComboBox;
    @ViewComponent
    private Scroller chatScroller;
    @ViewComponent
    private Div messageList;
    @ViewComponent
    private JmixTextArea promptInput;
    @ViewComponent
    private JmixButton sendButton;
    @ViewComponent
    private Span workflowTitle;
    @ViewComponent
    private Div workflowBadges;
    @ViewComponent
    private JmixButton jsonTextAreaCopy;
    @ViewComponent
    private JmixButton jsonDownload;
    @ViewComponent
    private JmixButton jsonTextAreaEdit;
    @ViewComponent
    private JmixButton diagramTabButton;
    @ViewComponent
    private JmixButton jsonTabButton;
    @ViewComponent
    private Div workflowBody;
    @ViewComponent
    private Html workflowEmpty;
    @ViewComponent
    private JmixCodeEditor jsonTextArea;

    private final ObjectMapper mapper = new ObjectMapper();
    private final WorkflowDiagram diagram = new WorkflowDiagram();

    private List<Conversation> conversations = new ArrayList<>();
    private Conversation currentConversation;
    private JsonDslSchema currentSchema;
    private WorkflowTab currentTab = WorkflowTab.DIAGRAM;
    private ZoneId zoneId = ZoneId.systemDefault();
    /** Узкий экран (телефон): панели идут друг под другом, список бесед прячется после выбора. */
    private boolean narrowScreen;

    /** Беседы, по которым ассистент ещё не ответил. */
    private final Set<UUID> pending = new HashSet<>();
    private final Map<UUID, Div> attachmentCards = new HashMap<>();
    private Component typingIndicator;

    @Subscribe
    public void onInit(final InitEvent event) {
        llmComboBox.setItems(llmServiceMap.values());
        llmComboBox.setItemLabelGenerator(LLMService::getModelCode);
        llmComboBox.setValue(defaultService());

        diagram.addClassName("vj-diagram");
        diagram.setVisible(false);
        workflowBody.add(diagram);

        conversationSearch.addValueChangeListener(e -> renderConversationList());

        // Enter отправляет сообщение, Shift+Enter переносит строку.
        Shortcuts.addShortcutListener(promptInput, this::submitPrompt, Key.ENTER).listenOn(promptInput);

        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            narrowScreen = details.getWindowInnerWidth() > 0 && details.getWindowInnerWidth() <= 900;
            if (narrowScreen) {
                workSplit.setOrientation(SplitLayout.Orientation.VERTICAL);
                workSplit.setSplitterPosition(60);
            }
            try {
                zoneId = ZoneId.of(details.getTimeZoneId());
                renderConversationList();
            } catch (Exception e) {
                log.debug("Browser time zone is not recognized: {}", details.getTimeZoneId());
            }
        });

        showTab(WorkflowTab.DIAGRAM);
        clearWorkflow();
        loadConversations();
        startNewChat();
    }

    // ------------------------------------------------------------------ Обработчики

    @Subscribe(id = "createConversation", subject = "clickListener")
    public void onCreateConversationClick(final ClickEvent<JmixButton> event) {
        startNewChat();
        hideConversationsOnNarrowScreen();
        promptInput.focus();
    }

    @Subscribe(id = "sendButton", subject = "clickListener")
    public void onSendButtonClick(final ClickEvent<JmixButton> event) {
        submitPrompt();
    }

    @Subscribe(id = "toggleConversations", subject = "clickListener")
    public void onToggleConversationsClick(final ClickEvent<JmixButton> event) {
        chatShell.setClassName("vj-conv-collapsed", !chatShell.hasClassName("vj-conv-collapsed"));
    }

    @Subscribe(id = "diagramTabButton", subject = "clickListener")
    public void onDiagramTabButtonClick(final ClickEvent<JmixButton> event) {
        showTab(WorkflowTab.DIAGRAM);
    }

    @Subscribe(id = "jsonTabButton", subject = "clickListener")
    public void onJsonTabButtonClick(final ClickEvent<JmixButton> event) {
        showTab(WorkflowTab.JSON);
    }

    @Subscribe(id = "jsonTextAreaCopy", subject = "clickListener")
    public void onJsonTextAreaCopyClick(final ClickEvent<JmixButton> event) {
        String json = jsonTextArea.getValue();
        if (StringUtils.isBlank(json)) {
            warn("Нечего копировать");
            return;
        }
        UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", json);
        success("JSON скопирован в буфер обмена");
    }

    @Subscribe(id = "jsonDownload", subject = "clickListener")
    public void onJsonDownloadClick(final ClickEvent<JmixButton> event) {
        String json = jsonTextArea.getValue();
        if (StringUtils.isBlank(json)) {
            warn("Нечего скачивать");
            return;
        }
        String fileName = fileNameOf(WorkflowSummary.of(json)) + ".json";
        UI.getCurrent().getPage().executeJs("""
                const url = URL.createObjectURL(new Blob([$0], {type: 'application/json'}));
                const link = document.createElement('a');
                link.href = url;
                link.download = $1;
                document.body.appendChild(link);
                link.click();
                link.remove();
                setTimeout(() => URL.revokeObjectURL(url), 1000);
                """, json, fileName);
    }

    @Subscribe(id = "jsonTextAreaEdit", subject = "clickListener")
    public void onJsonTextAreaEditClick(final ClickEvent<JmixButton> event) {
        if (currentSchema == null) {
            return;
        }
        openEditDialog();
    }

    // ------------------------------------------------------------------ Беседы

    private void loadConversations() {
        conversations = new ArrayList<>(dataManager.load(Conversation.class)
                .query("select e from Conversation e where e.createdBy = :username order by e.createdDate desc")
                .parameter("username", currentAuthentication.getUser().getUsername())
                .list());
        renderConversationList();
    }

    private void renderConversationList() {
        conversationList.removeAll();

        String filter = StringUtils.trimToEmpty(conversationSearch.getValue()).toLowerCase(RU);
        List<Conversation> visible = conversations.stream()
                .filter(c -> filter.isEmpty() || titleOf(c).toLowerCase(RU).contains(filter))
                .toList();

        if (visible.isEmpty()) {
            Div empty = new Div();
            empty.addClassName("vj-conv-empty");
            empty.setText(filter.isEmpty()
                    ? "Здесь появятся ваши беседы"
                    : "Ничего не найдено");
            conversationList.add(empty);
            return;
        }

        Map<String, List<Conversation>> groups = new LinkedHashMap<>();
        LocalDate today = LocalDate.now(zoneId);
        for (Conversation c : visible) {
            groups.computeIfAbsent(groupOf(c, today), k -> new ArrayList<>()).add(c);
        }
        groups.forEach((group, items) -> {
            Span caption = new Span(group);
            caption.addClassName("vj-conv-group");
            conversationList.add(caption);
            items.forEach(c -> conversationList.add(createConversationItem(c)));
        });
    }

    private Component createConversationItem(Conversation conversation) {
        Div item = new Div();
        item.addClassName("vj-conv-item");
        if (isCurrent(conversation)) {
            item.addClassName("active");
        }

        Icon icon = VaadinIcon.CHAT.create();
        icon.addClassName("vj-conv-icon");

        Span title = new Span(titleOf(conversation));
        title.addClassName("vj-conv-title");

        Span meta = new Span();
        meta.addClassName("vj-conv-meta");
        if (pending.contains(conversation.getId())) {
            meta.setText("агент отвечает…");
            meta.addClassName("pending");
        } else {
            meta.setText(shortDateOf(conversation.getCreatedDate()));
        }

        Div text = new Div(title, meta);
        text.addClassName("vj-conv-text");

        Button menuButton = new Button(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ICON);
        menuButton.addClassName("vj-conv-menu");
        menuButton.setAriaLabel("Действия с беседой");
        // Щелчок по меню не должен заодно открывать беседу.
        menuButton.getElement().addEventListener("click", e -> {
        }).addEventData("event.stopPropagation()");

        ContextMenu menu = new ContextMenu(menuButton);
        menu.setOpenOnClick(true);
        menu.addItem(menuItem(VaadinIcon.PENCIL, "Переименовать"), e -> openRenameDialog(conversation));
        menu.addItem(menuItem(VaadinIcon.TRASH, "Удалить"), e -> openDeleteDialog(conversation))
                .addClassName("vj-danger-item");

        item.add(icon, text, menuButton);
        item.addClickListener(e -> {
            selectConversation(conversation);
            hideConversationsOnNarrowScreen();
        });
        return item;
    }

    private void hideConversationsOnNarrowScreen() {
        if (narrowScreen) {
            chatShell.addClassName("vj-conv-collapsed");
        }
    }

    private Component menuItem(VaadinIcon icon, String text) {
        Div content = new Div(icon.create(), new Span(text));
        content.addClassName("vj-menu-item");
        return content;
    }

    private void startNewChat() {
        currentConversation = null;
        llmComboBox.setValue(defaultService());
        renderWelcome();
        clearWorkflow();
        updateChatHeader(0);
        updateComposerState();
        renderConversationList();
    }

    private void selectConversation(Conversation conversation) {
        currentConversation = conversation;
        llmComboBox.setValue(Optional.ofNullable(conversation.getService())
                .map(llmServiceMap::get)
                .orElse(defaultService()));

        List<ChatMessage> messages = loadMessages(conversation);
        renderMessages(messages);

        messages.stream()
                .map(ChatMessage::getJsonDslSchema)
                .filter(Objects::nonNull)
                .reduce((first, second) -> second)
                .ifPresentOrElse(this::showWorkflow, this::clearWorkflow);

        if (pending.contains(conversation.getId())) {
            showTyping();
        }
        updateChatHeader(messages.size());
        updateComposerState();
        renderConversationList();
        scrollToBottom();
    }

    private void openRenameDialog(Conversation conversation) {
        dialogs.createInputDialog(this)
                .withHeader("Переименовать беседу")
                .withParameters(InputParameter.stringParameter("title")
                        .withLabel("Название")
                        .withDefaultValue(titleOf(conversation))
                        .withRequired(true))
                .withLabelsPosition(Dialogs.InputDialogBuilder.LabelsPosition.TOP)
                .withWidth("28rem")
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (!closeEvent.closedWith(DialogOutcome.OK)) {
                        return;
                    }
                    String title = StringUtils.abbreviate(
                            StringUtils.trimToEmpty(closeEvent.getValue("title")), TITLE_MAX_LENGTH);
                    if (title.isEmpty()) {
                        return;
                    }
                    conversation.setTitle(title);
                    replaceConversation(dataManager.save(conversation));
                })
                .open();
    }

    private void openDeleteDialog(Conversation conversation) {
        dialogs.createOptionDialog()
                .withHeader("Удалить беседу?")
                .withText("Беседа «" + titleOf(conversation) + "» и все её сообщения будут удалены "
                        + "без возможности восстановления.")
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withText("Удалить")
                                .withVariant(ActionVariant.DANGER)
                                .withHandler(e -> deleteConversation(conversation)),
                        new DialogAction(DialogAction.Type.NO)
                                .withText("Отмена"))
                .open();
    }

    private void deleteConversation(Conversation conversation) {
        dataManager.remove(conversation);
        conversations.removeIf(c -> c.getId().equals(conversation.getId()));
        if (isCurrent(conversation)) {
            startNewChat();
        } else {
            renderConversationList();
        }
        success("Беседа удалена");
    }

    private void replaceConversation(Conversation saved) {
        conversations.replaceAll(c -> c.getId().equals(saved.getId()) ? saved : c);
        if (isCurrent(saved)) {
            currentConversation = saved;
            updateChatHeader(renderedMessageCount());
        }
        renderConversationList();
    }

    private Conversation ensureConversation(String firstPrompt) {
        if (currentConversation != null) {
            return currentConversation;
        }
        Conversation conversation = metadata.create(Conversation.class);
        conversation.setTitle(titleFromPrompt(firstPrompt));
        LLMService service = llmComboBox.getValue();
        conversation.setService(service != null ? service.getModelCode() : DEFAULT_MODEL);
        conversation = dataManager.save(conversation);

        conversations.add(0, conversation);
        currentConversation = conversation;
        renderConversationList();
        return conversation;
    }

    // ------------------------------------------------------------------ Сообщения

    private List<ChatMessage> loadMessages(Conversation conversation) {
        return dataManager.load(ChatMessage.class)
                .query("select e from ChatMessage e where e.conversation = :conversation order by e.createdDate")
                .parameter("conversation", conversation)
                .fetchPlan(fp -> fp.addFetchPlan(FetchPlan.BASE).add("jsonDslSchema", FetchPlan.BASE))
                .list();
    }

    private void renderMessages(List<ChatMessage> messages) {
        messageList.removeAll();
        messageList.removeClassName("welcome");
        attachmentCards.clear();
        typingIndicator = null;

        if (messages.isEmpty()) {
            renderWelcome();
            return;
        }
        messages.forEach(m -> messageList.add(createMessage(m, m.getJsonDslSchema())));
    }

    private void renderWelcome() {
        messageList.removeAll();
        attachmentCards.clear();
        typingIndicator = null;
        messageList.addClassName("welcome");

        Image logo = new Image("icons/logo.svg", "");
        logo.addClassName("vj-welcome-logo");

        H2 title = new H2("Чем помочь с интеграцией?");
        title.addClassName("vj-welcome-title");

        Paragraph text = new Paragraph("Опишите бизнес-процесс своими словами: откуда приходят данные, "
                + "что с ними сделать и куда отправить. Агент уточнит детали и соберёт workflow.");
        text.addClassName("vj-welcome-text");

        Div cards = new Div();
        cards.addClassName("vj-suggestions");
        SUGGESTIONS.forEach(s -> {
            Icon icon = s.icon().create();
            icon.addClassName("vj-suggestion-icon");
            Span cardTitle = new Span(s.title());
            cardTitle.addClassName("vj-suggestion-title");
            Span cardText = new Span(s.prompt());
            cardText.addClassName("vj-suggestion-text");
            Div card = new Div(icon, cardTitle, cardText);
            card.addClassName("vj-suggestion");
            card.addClickListener(e -> {
                promptInput.setValue(s.prompt());
                promptInput.focus();
            });
            cards.add(card);
        });

        Div welcome = new Div(logo, title, text, cards);
        welcome.addClassName("vj-welcome");
        messageList.add(welcome);
    }

    private Component createMessage(ChatMessage message, JsonDslSchema schema) {
        boolean fromUser = message.getSenderType() == SenderType.USER;

        Div bubble = new Div();
        bubble.addClassName("vj-msg-bubble");
        if (fromUser) {
            bubble.setText(StringUtils.defaultString(message.getContent()));
        } else {
            bubble.add(new Html(MarkdownRenderer.toHtml(message.getContent())));
        }

        Div body = new Div(createMessageMeta(fromUser ? userDisplayName() : "Vibe JSON", message.getCreatedDate()),
                bubble);
        body.addClassName("vj-msg-body");
        if (schema != null) {
            body.add(createAttachment(schema));
        }

        Div row = new Div(createAvatar(fromUser), body);
        row.addClassNames("vj-msg", fromUser ? "vj-msg-user" : "vj-msg-bot");
        return row;
    }

    private Component createMessageMeta(String author, OffsetDateTime createdDate) {
        Span name = new Span(author);
        name.addClassName("vj-msg-author");
        Span time = new Span(timeOf(createdDate));
        time.addClassName("vj-msg-time");
        Div meta = new Div(name, time);
        meta.addClassName("vj-msg-meta");
        return meta;
    }

    private Component createAvatar(boolean fromUser) {
        if (fromUser) {
            Avatar avatar = new Avatar(userDisplayName());
            avatar.addClassName("vj-msg-avatar");
            return avatar;
        }
        Image logo = new Image("icons/logo.svg", "Vibe JSON");
        logo.addClassNames("vj-msg-avatar", "vj-bot-avatar");
        return logo;
    }

    private Component createAttachment(JsonDslSchema schema) {
        WorkflowSummary summary = WorkflowSummary.of(schema.getSchemaText());

        Div iconBox = new Div(VaadinIcon.SITEMAP.create());
        iconBox.addClassName("vj-attachment-icon");

        Span title = new Span(summary.displayName());
        title.addClassName("vj-attachment-title");
        Span details = new Span("Схема workflow · " + summary.stepCount() + " "
                + plural(summary.stepCount(), "шаг", "шага", "шагов"));
        details.addClassName("vj-attachment-details");
        Div text = new Div(title, details);
        text.addClassName("vj-attachment-text");

        Icon open = VaadinIcon.ARROW_RIGHT.create();
        open.addClassName("vj-attachment-open");

        Div card = new Div(iconBox, text, open);
        card.addClassName("vj-attachment");
        card.getElement().setAttribute("title", "Показать эту версию схемы");
        card.addClickListener(e -> showWorkflow(schema));
        if (currentSchema != null && currentSchema.getId().equals(schema.getId())) {
            card.addClassName("active");
        }
        attachmentCards.put(schema.getId(), card);
        return card;
    }

    private void showTyping() {
        if (typingIndicator != null) {
            return;
        }
        Div dots = new Div(new Span(), new Span(), new Span());
        dots.addClassName("vj-typing-dots");
        Span label = new Span("Агент собирает workflow");
        label.addClassName("vj-typing-label");
        Div bubble = new Div(dots, label);
        bubble.addClassNames("vj-msg-bubble", "vj-typing-bubble");

        Div body = new Div(createMessageMeta("Vibe JSON", null), bubble);
        body.addClassName("vj-msg-body");
        Div row = new Div(createAvatar(false), body);
        row.addClassNames("vj-msg", "vj-msg-bot", "vj-typing");
        typingIndicator = row;
        messageList.add(row);
        scrollToBottom();
    }

    private void hideTyping() {
        if (typingIndicator != null) {
            messageList.remove(typingIndicator);
            typingIndicator = null;
        }
    }

    private Component createErrorMessage(String error, Runnable retry) {
        Span text = new Span("Не удалось получить ответ ассистента. " + error);
        Button retryButton = new Button("Повторить", VaadinIcon.REFRESH.create());
        retryButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        Div bubble = new Div(text, retryButton);
        bubble.addClassNames("vj-msg-bubble", "vj-error-bubble");

        Div body = new Div(createMessageMeta("Vibe JSON", OffsetDateTime.now()), bubble);
        body.addClassName("vj-msg-body");
        Div row = new Div(createAvatar(false), body);
        row.addClassNames("vj-msg", "vj-msg-bot", "vj-msg-error");
        retryButton.addClickListener(e -> {
            messageList.remove(row);
            retry.run();
        });
        return row;
    }

    /** Число настоящих сообщений на экране, без индикатора набора и карточек ошибок. */
    private long renderedMessageCount() {
        return messageList.getChildren()
                .filter(c -> c.hasClassName("vj-msg"))
                .filter(c -> !c.hasClassName("vj-typing") && !c.hasClassName("vj-msg-error"))
                .count();
    }

    private void scrollToBottom() {
        chatScroller.getElement().executeJs(
                "const s = this; requestAnimationFrame(() => s.scrollTo({top: s.scrollHeight, behavior: 'smooth'}));");
    }

    // ------------------------------------------------------------------ Отправка запроса

    private void submitPrompt() {
        String prompt = StringUtils.strip(promptInput.getValue());
        if (StringUtils.isEmpty(prompt)) {
            return;
        }
        LLMService llmService = llmComboBox.getValue();
        if (llmService == null) {
            warn("Выберите модель");
            return;
        }
        if (currentConversation != null && pending.contains(currentConversation.getId())) {
            warn("Дождитесь ответа на предыдущее сообщение");
            return;
        }

        boolean firstMessage = currentConversation == null || messageList.hasClassName("welcome");
        Conversation conversation = ensureConversation(prompt);
        if (firstMessage && DEFAULT_TITLES.contains(titleOf(conversation))) {
            conversation.setTitle(titleFromPrompt(prompt));
            conversation = dataManager.save(conversation);
            currentConversation = conversation;
            replaceConversation(conversation);
        }

        ChatMessage userMessage = saveUserMessage(conversation, prompt);
        if (messageList.hasClassName("welcome")) {
            messageList.removeAll();
            messageList.removeClassName("welcome");
        }
        messageList.add(createMessage(userMessage, null));
        promptInput.clear();

        requestReply(llmService, conversation, prompt);
    }

    private ChatMessage saveUserMessage(Conversation conversation, String prompt) {
        ChatMessage message = metadata.create(ChatMessage.class);
        message.setConversation(conversation);
        message.setSenderType(SenderType.USER);
        message.setContent(prompt);
        return dataManager.save(message);
    }

    private void requestReply(LLMService llmService, Conversation conversation, String prompt) {
        pending.add(conversation.getId());
        showTyping();
        updateComposerState();
        updateChatHeader(renderedMessageCount());
        renderConversationList();

        UI ui = UI.getCurrent();
        String username = currentAuthentication.getUser().getUsername();

        // Ответ сохраняется в фоновом потоке, а не при отрисовке: пользователь
        // может закрыть вкладку, не дождавшись модели, и ответ не должен теряться.
        CompletableFuture
                .supplyAsync(() -> systemAuthenticator.withUser(username, () -> {
                    long startedAt = System.currentTimeMillis();
                    log.info("---> Prompt process started; conversationId = {}", conversation.getId());
                    ChatMessageAndWorkflow dto = llmService.userPromptToWorkflow(conversation, prompt);
                    log.info("---> Prompt process finished (took {} ms); conversationId = {}",
                            System.currentTimeMillis() - startedAt, conversation.getId());
                    return saveReply(conversation, dto);
                }), llmExecutor)
                .whenComplete((reply, ex) -> ui.access(() -> onReply(llmService, conversation, prompt, reply, ex)));
    }

    private Reply saveReply(Conversation conversation, ChatMessageAndWorkflow dto) {
        ChatMessage message = metadata.create(ChatMessage.class);
        message.setConversation(conversation);
        message.setSenderType(SenderType.BOT);
        message.setContent(dto.getLLMChatMsg());

        JsonDslSchema schema = null;
        if (StringUtils.isNotBlank(dto.getWorkflow())) {
            schema = metadata.create(JsonDslSchema.class);
            schema.setSchemaText(dto.getWorkflow());
            message.setJsonDslSchema(schema);
        }

        if (schema == null) {
            return new Reply(dataManager.save(message), null);
        }
        EntitySet saved = dataManager.save(schema, message);
        return new Reply(saved.get(message), saved.get(schema));
    }

    private void onReply(LLMService llmService, Conversation conversation, String prompt,
                         Reply reply, Throwable exception) {
        pending.remove(conversation.getId());
        boolean current = isCurrent(conversation);
        renderConversationList();

        if (current) {
            hideTyping();
            updateComposerState();
        }

        if (exception != null) {
            Throwable cause = exception instanceof CompletionException && exception.getCause() != null
                    ? exception.getCause() : exception;
            log.error("LLM request failed; conversationId = {}", conversation.getId(), cause);
            String error = StringUtils.abbreviate(StringUtils.defaultString(cause.getMessage(), cause.toString()), 300);
            if (current) {
                messageList.add(createErrorMessage(error, () -> requestReply(llmService, conversation, prompt)));
                scrollToBottom();
            } else {
                notifications.create("Ошибка LLM в беседе «" + titleOf(conversation) + "»", error)
                        .withType(Notifications.Type.ERROR)
                        .show();
            }
            return;
        }

        if (!current) {
            notifications.create("Ассистент ответил в беседе «" + titleOf(conversation) + "»")
                    .withType(Notifications.Type.SUCCESS)
                    .withPosition(Notification.Position.BOTTOM_END)
                    .withDuration(4000)
                    .show();
            return;
        }

        if (reply.schema() != null) {
            currentSchema = reply.schema();
        }
        messageList.add(createMessage(reply.message(), reply.schema()));
        if (reply.schema() != null) {
            showWorkflow(reply.schema());
        }
        updateChatHeader(renderedMessageCount());
        scrollToBottom();
        promptInput.focus();
    }

    private void updateComposerState() {
        boolean busy = currentConversation != null && pending.contains(currentConversation.getId());
        sendButton.setEnabled(!busy);
        promptInput.setPlaceholder(busy
                ? "Агент готовит ответ…"
                : "Опишите процесс или ответьте на вопрос агента");
    }

    private void updateChatHeader(long messageCount) {
        if (currentConversation == null) {
            chatTitle.setText(DEFAULT_TITLE);
            chatSubtitle.setText("Опишите процесс - агент соберёт workflow");
            return;
        }
        chatTitle.setText(titleOf(currentConversation));
        chatSubtitle.setText(messageCount + " " + plural((int) messageCount, "сообщение", "сообщения", "сообщений")
                + " · начата " + dayTimeOf(currentConversation.getCreatedDate()));
    }

    // ------------------------------------------------------------------ Схема

    private void showWorkflow(JsonDslSchema schema) {
        currentSchema = schema;
        String json = formatJson(schema.getSchemaText());
        WorkflowSummary summary = WorkflowSummary.of(json);

        jsonTextArea.setValue(json);
        diagram.setWorkflow(json);

        workflowTitle.setText(summary.displayName());
        workflowBadges.removeAll();
        workflowBadges.add(badge(summary.stepCount() + " " + plural(summary.stepCount(), "шаг", "шага", "шагов"),
                "accent"));
        summary.starters().forEach(s -> workflowBadges.add(badge("старт: " + s, "starter")));
        if (schema.getCreatedDate() != null) {
            workflowBadges.add(badge("версия от " + dayTimeOf(schema.getCreatedDate()), null));
        }

        workflowEmpty.setVisible(false);
        setWorkflowActionsEnabled(true);
        showTab(currentTab);

        attachmentCards.forEach((id, card) -> card.setClassName("active", id.equals(schema.getId())));
    }

    private void clearWorkflow() {
        currentSchema = null;
        jsonTextArea.clear();
        diagram.setWorkflow(null);
        workflowTitle.setText("Схема workflow");
        workflowBadges.removeAll();
        workflowBadges.add(badge("ещё не сформирована", null));
        workflowEmpty.setVisible(true);
        diagram.setVisible(false);
        jsonTextArea.setVisible(false);
        setWorkflowActionsEnabled(false);
    }

    private void setWorkflowActionsEnabled(boolean enabled) {
        jsonTextAreaCopy.setEnabled(enabled);
        jsonDownload.setEnabled(enabled);
        jsonTextAreaEdit.setEnabled(enabled);
    }

    private void showTab(WorkflowTab tab) {
        currentTab = tab;
        diagramTabButton.setClassName("active", tab == WorkflowTab.DIAGRAM);
        jsonTabButton.setClassName("active", tab == WorkflowTab.JSON);
        boolean hasSchema = currentSchema != null;
        diagram.setVisible(hasSchema && tab == WorkflowTab.DIAGRAM);
        jsonTextArea.setVisible(hasSchema && tab == WorkflowTab.JSON);
    }

    private Span badge(String text, String variant) {
        Span badge = new Span(text);
        badge.addClassName("vj-badge");
        if (variant != null) {
            badge.addClassName(variant);
        }
        return badge;
    }

    private void openEditDialog() {
        JsonDslSchema schema = currentSchema;

        JmixCodeEditor editor = uiComponents.create(JmixCodeEditor.class);
        editor.setMode(CodeEditorMode.JSON);
        editor.setTheme(CodeEditorTheme.ONE_DARK);
        editor.setShowPrintMargin(false);
        editor.setFontSize("13px");
        editor.setValue(jsonTextArea.getValue());
        editor.setSizeFull();

        Span error = new Span();
        error.addClassName("vj-dialog-error");

        Dialog dialog = new Dialog();
        dialog.addClassName("vj-edit-dialog");
        dialog.setHeaderTitle("Редактирование схемы");
        dialog.setWidth("min(1000px, 95vw)");
        dialog.setHeight("85vh");
        dialog.setDraggable(true);
        dialog.setResizable(true);

        Div content = new Div(editor, error);
        content.addClassName("vj-edit-dialog-content");
        dialog.add(content);

        Button format = new Button("Форматировать", VaadinIcon.ALIGN_LEFT.create(), e -> {
            try {
                editor.setValue(formatJsonStrict(editor.getValue()));
                error.setText("");
            } catch (JsonProcessingException ex) {
                error.setText("JSON невалиден: " + ex.getOriginalMessage());
            }
        });
        format.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        format.getStyle().set("margin-inline-end", "auto");

        Button cancel = new Button("Отмена", e -> dialog.close());
        Button save = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> {
            String json;
            try {
                json = formatJsonStrict(editor.getValue());
            } catch (JsonProcessingException ex) {
                error.setText("JSON невалиден: " + ex.getOriginalMessage());
                return;
            }
            schema.setSchemaText(json);
            JsonDslSchema saved = dataManager.save(schema);
            dialog.close();
            if (currentConversation != null) {
                renderMessages(loadMessages(currentConversation));
            }
            showWorkflow(saved);
            success("Схема сохранена");
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(format, cancel, save);
        dialog.open();
    }

    // ------------------------------------------------------------------ Вспомогательное

    private LLMService defaultService() {
        return Optional.ofNullable(llmServiceMap.get(DEFAULT_MODEL))
                .orElseGet(() -> llmServiceMap.values().stream().findFirst().orElse(null));
    }

    private boolean isCurrent(Conversation conversation) {
        return currentConversation != null && currentConversation.getId().equals(conversation.getId());
    }

    private String titleOf(Conversation conversation) {
        return StringUtils.defaultIfBlank(conversation.getTitle(), DEFAULT_TITLE);
    }

    private String titleFromPrompt(String prompt) {
        String firstLine = StringUtils.trimToEmpty(prompt).lines().findFirst().orElse("");
        return StringUtils.defaultIfBlank(StringUtils.abbreviate(firstLine, TITLE_MAX_LENGTH), DEFAULT_TITLE);
    }

    private String userDisplayName() {
        UserDetails user = currentAuthentication.getUser();
        if (user instanceof User u) {
            String fullName = StringUtils.trimToEmpty(
                    StringUtils.defaultString(u.getFirstName()) + " " + StringUtils.defaultString(u.getLastName()));
            if (!fullName.isEmpty()) {
                return fullName;
            }
        }
        return user.getUsername();
    }

    private String groupOf(Conversation conversation, LocalDate today) {
        if (conversation.getCreatedDate() == null) {
            return "Сегодня";
        }
        LocalDate day = conversation.getCreatedDate().atZoneSameInstant(zoneId).toLocalDate();
        long days = ChronoUnit.DAYS.between(day, today);
        if (days <= 0) {
            return "Сегодня";
        }
        if (days == 1) {
            return "Вчера";
        }
        if (days < 7) {
            return "Последние 7 дней";
        }
        return "Ранее";
    }

    private String shortDateOf(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zoned = dateTime.atZoneSameInstant(zoneId);
        LocalDate today = LocalDate.now(zoneId);
        if (zoned.toLocalDate().equals(today)) {
            return TIME.format(zoned);
        }
        if (zoned.toLocalDate().equals(today.minusDays(1))) {
            return "вчера, " + TIME.format(zoned);
        }
        return DAY.format(zoned);
    }

    private String timeOf(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zoned = dateTime.atZoneSameInstant(zoneId);
        return zoned.toLocalDate().equals(LocalDate.now(zoneId)) ? TIME.format(zoned) : DAY_TIME.format(zoned);
    }

    private String dayTimeOf(OffsetDateTime dateTime) {
        return dateTime == null ? "" : DAY_TIME.format(dateTime.atZoneSameInstant(zoneId));
    }

    private static String plural(int n, String one, String few, String many) {
        int mod10 = n % 10;
        int mod100 = n % 100;
        if (mod10 == 1 && mod100 != 11) {
            return one;
        }
        if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14)) {
            return few;
        }
        return many;
    }

    private static String fileNameOf(WorkflowSummary summary) {
        String name = summary.name() == null ? "workflow" : summary.name();
        String safe = name.replaceAll("[^\\p{L}\\p{N}._-]+", "-").replaceAll("(^-+|-+$)", "");
        return safe.isEmpty() ? "workflow" : safe;
    }

    private String formatJson(String rawJson) {
        try {
            return formatJsonStrict(rawJson);
        } catch (Exception e) {
            return rawJson;
        }
    }

    private String formatJsonStrict(String rawJson) throws JsonProcessingException {
        Object json = mapper.readValue(rawJson, Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    private void warn(String text) {
        notifications.create(text)
                .withType(Notifications.Type.WARNING)
                .withDuration(3_000)
                .show();
    }

    private void success(String text) {
        notifications.create(text)
                .withType(Notifications.Type.SUCCESS)
                .withPosition(Notification.Position.BOTTOM_END)
                .withDuration(2_000)
                .show();
    }
}
