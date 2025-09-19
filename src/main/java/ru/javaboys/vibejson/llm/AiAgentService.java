package ru.javaboys.vibejson.llm;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemoryConfig;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiAgentService {

    private final ChatClient chatClient;
    private final WorkflowTools workflowTools;

    public AiAgentService(ChatClient.Builder chatClientBuilder, JdbcTemplate jdbcTemplate, WorkflowTools workflowTools) {
        this.workflowTools = workflowTools;
        var chatMemory = JdbcChatMemory.create(JdbcChatMemoryConfig.builder().jdbcTemplate(jdbcTemplate).build());
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public LLMResponseDto processUserMessage(String sessionId, String userMessage, String currentWorkflow) {
        Message systemMsg = createSystemInstruction(currentWorkflow);
        Message userMsg = new UserMessage(userMessage);

        List<Message> promptMessages = new ArrayList<>();
        promptMessages.add(systemMsg);
        promptMessages.add(userMsg);

        return chatClient
                .prompt(new Prompt(promptMessages))
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", sessionId)
                        .param("chat_memory_response_size", 100))
                .tools(workflowTools)
                .call()
                .entity(LLMResponseDto.class);
    }

    @SneakyThrows
    private Message createSystemInstruction(String currentWorkflow) {

        String systemText = """
                Ты – AI помощник для построения интеграционных бизнес-процессов.
                Ты можешь обсуждать только вопросы и задачи связанные с формированием workflow.
                Твоя основная задача - преобразовывать интеграционный бизнес-процесс, описанный пользователем с помощью текста, в структуру workflow.
                Допустимые типы Starter (стартеры): {allowedStarterTypes}.
                Допустимые типы Activity (активити): {allowedActivityTypes}.
                Активити - это те кубики, из которых строится основная логика работы интеграционных бизнес-процессов.
                Стартеры - это способ запуска интеграционного бизнес-процесса.
                
                Твой алгоритм по формированию workflow из текстового описания пользователя:
                - Построй список активити в соответствии с доступными активити.
                Сам выбирай активити в соответствии с описанием интеграционного бизнес-процесса, который указывает пользователь и названием активити.
                В соответствии со структурой требуемых активити, описанных в workflow, уточни параметры недостающие для заполнения полей активити.
                - Задай вопрос какие стартеры использовать для запуска интеграционного бизнес-процесса, над которым ты работаешь.
                В соответствии со структурой требуемого стартеров, описанного в workflow, уточни параметры недостающие для заполнения полей стартера.
                - Если не хватает данных (например, отсутствует обязательный параметр), задавай уточняющие вопросы.
                - Некоторые поля workflow можешь заполнять подходящими по контексту значения на своё усмотрение.
                - Для валидации workflow используй инструмент validateWorkflow, который возвращает признак валидности и список ошибок, если workflow не валидный.
                Если были обнаружены ошибки валидации, то на основе описания этих ошибок сформируй дополнительные вопросы для пользователя,
                чтобы на их основе пользователь уточнил или исправил параметры workflow, что позволило бы пройти валидацию.
                - На каждом шаге возвращай сообщение для пользователя в поле chatMessageForUser.
                - Возвращай workflow только, если он был полностью сформирован. В противном случае, не заполняй это поле.
                """;

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("allowedStarterTypes", String.join(",", WorkflowUtils.getAllowedStarterTypes()));
        templateParams.put("allowedActivityTypes", String.join(",", WorkflowUtils.getAllowedActivityTypes()));

        if (currentWorkflow != null && currentWorkflow.isBlank()) {
            systemText += """
                    Текущий, сформированный ранее, Workflow: ```
                    {currentWorkflow}
                    ```
                    На основе текущего workflow выполни запрос пользователя.
                    """;
            templateParams.put("currentWorkflow", currentWorkflow);
        }

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        return systemPromptTemplate.createMessage(templateParams);
    }
}
