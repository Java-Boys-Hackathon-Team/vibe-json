package ru.javaboys.vibejson.security;

import io.jmix.core.entity.KeyValueEntity;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.annotation.SpecificPolicy;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;
import ru.javaboys.vibejson.entity.ChatMessage;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.JsonDslSchema;

@ResourceRole(name = "Employee", code = EmployeeRole.CODE)
public interface EmployeeRole {
    String CODE = "employee";

    @MenuPolicy(menuIds = "VibeJsonChatView")
    @ViewPolicy(viewIds = {"VibeJsonChatView", "LoginView", "MainView", "Conversation.detail"})
    @SpecificPolicy(resources = "ui.loginToUi")
    void screens();

    @EntityPolicy(entityClass = Conversation.class, actions = EntityPolicyAction.ALL)
    @EntityAttributePolicy(entityClass = Conversation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void conversation();

    @EntityPolicy(entityClass = ChatMessage.class, actions = EntityPolicyAction.ALL)
    @EntityAttributePolicy(entityClass = ChatMessage.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void chatMessage();

    @EntityPolicy(entityClass = JsonDslSchema.class, actions = EntityPolicyAction.ALL)
    @EntityAttributePolicy(entityClass = JsonDslSchema.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void jsonDslSchema();

    @EntityPolicy(entityClass = KeyValueEntity.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = KeyValueEntity.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void keyValueEntity();
}