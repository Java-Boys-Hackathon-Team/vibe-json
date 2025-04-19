package ru.javaboys.vibejson.view.conversation;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.view.main.MainView;

@Route(value = "conversations/:id", layout = MainView.class)
@ViewController(id = "Conversation.detail")
@ViewDescriptor(path = "conversation-detail-view.xml")
@EditedEntityContainer("conversationDc")
public class ConversationDetailView extends StandardDetailView<Conversation> {
}