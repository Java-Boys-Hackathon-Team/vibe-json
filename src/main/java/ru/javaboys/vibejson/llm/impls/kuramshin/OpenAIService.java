package ru.javaboys.vibejson.llm.impls.kuramshin;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemoryConfig;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.llm.impls.kuramshin.dto.RespDto;
import ru.javaboys.vibejson.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIService {

    private final ChatClient chatClient;
    private final WorkflowKnowledgeBase knowledgeBase;
    private String workflowJsonSchema;

    public OpenAIService(ChatClient.Builder chatClientBuilder, JdbcTemplate jdbcTemplate, WorkflowKnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
        var chatMemory = JdbcChatMemory.create(JdbcChatMemoryConfig.builder().jdbcTemplate(jdbcTemplate).build());
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @PostConstruct
    @SneakyThrows
    public void loadSchemaOnce() {
        var workflowFromClassPath = CommonUtils.readFileFromClassPath("workflow-schema.json");
        this.workflowJsonSchema = CommonUtils.minifyJson(workflowFromClassPath);
    }

    public RespDto processUserMessage(String sessionId, String userMessage, String currentWorkflow) {

        Message systemMsg = createSystemInstruction(currentWorkflow);
        Message userMsg = new UserMessage(userMessage);

        List<Message> promptMessages = new ArrayList<>();
        promptMessages.add(systemMsg);
        promptMessages.add(userMsg);

        String fullResponse = chatClient
                .prompt(new Prompt(promptMessages))
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", sessionId)
                        .param("chat_memory_response_size", 100))
                .tools(knowledgeBase)
                .call()
                .content();

        String workflowJson = extractJsonFromText(fullResponse);
        RespDto result = new RespDto();
        if (workflowJson != null) {
            result.setWorkflowJson(workflowJson);
        }

        result.setAssistantMessage(fullResponse);
        return result;
    }

    @SneakyThrows
    private Message createSystemInstruction(String currentWorkflow) {
        String allowedStarterTypes = String.join(", ", knowledgeBase.getAllowedStarterTypes());
        String allowedActivityTypes = String.join(", ", knowledgeBase.getAllowedActivityTypes());

        String sysText = """
        Ты – AI помощник для построения интеграционных workflow (интеграционных бизнес-процессов).
        Ты можешь обсуждать только вопросы и задачи связанные с формированием workflow.
        Формат workflow – это JSON в соответствии со следующей json schema: %s.
        Workflow, которые ты создаешь или редактируешь должны строго соответствовать этой json schema - это ключевое правило.
        Твоя основная задача - преобразовывать workflow описанный с помощью текста в workflow в формате json в соответствии с json schema.
        Допустимые типы Starter (стартеры): %s.
        Допустимые типы Activity (активити): %s.
        Активити - это те кубики, из которых строится основная логика работы workflow.
        Стартеры - это способ запуска текущего workflow.
       
        Твой алгоритм по преобразование текстового описания workflow в json:
        1) Построй цепочку активити в соответствии с доступными активити.
        В соответствии с json schema уточни параметры недостающие для заполнения полей активити.
        2) Задай вопрос какие стартеры использовать для запуска workflow, над которым ты работаешь.
        В соответствии с json schema уточни параметры недостающие для заполнения полей стартера.
        3) Если не хватает данных (например, отсутствует обязательный параметр), задавай уточняющие вопросы.
        4) Некоторые поля workflow можешь заполнять подходящими по контексту значения на своё усмотрение в соответствии с json schema.
        Когда результат готов, то выводи его как JSON в соответствии с приведенной json schema.
        Результат обрамляй в "```json {результат в формате JSON в соответствии с json schema}```
        "
        """.formatted(workflowJsonSchema, allowedStarterTypes, allowedActivityTypes);

        if (currentWorkflow != null && currentWorkflow.isBlank()) {
            sysText += """
            Текущий Workflow JSON: ```
            %s
            ```
            На основе текущего workflow выполни запрос пользователя.
            """.formatted(currentWorkflow);
        }

        return new SystemMessage(sysText);
    }

    private String extractJsonFromText(String text) {
        String startMarker = "```json";
        String endMarker = "```";

        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) return null;

        int jsonStart = startIndex + startMarker.length();
        int endIndex = text.indexOf(endMarker, jsonStart);
        if (endIndex == -1) return null;

        String json = text.substring(jsonStart, endIndex).trim();
        return json;
    }
}
