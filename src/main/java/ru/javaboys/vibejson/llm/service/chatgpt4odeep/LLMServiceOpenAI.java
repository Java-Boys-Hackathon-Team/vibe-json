package ru.javaboys.vibejson.llm.service.chatgpt4odeep;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.wfdefenition.dto2.WorkflowDefinitionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LLMServiceOpenAI {

    private final ChatClient chatClient;

    public LLMServiceOpenAI(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

//    @Autowired
//    private OpenAiChatModel chatModel;
    // Альтернативно можно использовать ChatClient, тогда:  @Autowired private ChatClient chatClient;
    @Autowired
    private WorkflowKnowledgeBase knowledgeBase;

    // Храним состояния диалогов в памяти (для простоты – в мапе)
    private final Map<String, WorkflowDefinitionDto> currentWorkflows = new ConcurrentHashMap<>();
    private final Map<String, List<Message>> conversationHistories = new ConcurrentHashMap<>();

    public LLMRespDto processUserMessage(String sessionId, String userMessage) {
        // Инициализация истории диалога, если новая сессия
        conversationHistories.putIfAbsent(sessionId, new ArrayList<>());
        List<Message> history = conversationHistories.get(sessionId);

        // Создаём system-подсказку с инструкциями (в начале диалога или обновляем)
        Message systemMsg = createSystemInstruction(sessionId);
        Message userMsg = new UserMessage(userMessage);
        // Формируем полный prompt: система + история + новое сообщение пользователя
        List<Message> promptMessages = new ArrayList<>();
        promptMessages.add(systemMsg);
        // Добавляем предыдущие ассистентские и пользовательские сообщения из history, если нужно контекст (например, для продолжения незавершённого уточнения)
        if (!history.isEmpty()) {
            promptMessages.addAll(history);
        }
        promptMessages.add(userMsg);

//        // Настраиваем опции GPT: указываем, какие инструменты может вызывать
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4o")  // указываем модель GPT-4 (или ее deployment name, если Azure OpenAI)
                .temperature(0.2) // относительно низкая температура для предсказуемости
                .tools(List.of()) // разрешаем использование наших функций
                .build();

        // Вызываем модель с подготовленным prompt

        String fullResponse = chatClient.prompt(new Prompt(promptMessages)).tools(knowledgeBase).call().content();  // полный текст ответа ассистента

        // Обновляем историю диалога: добавляем последнее сообщение пользователя и ассистента
        history.add(userMsg);
        history.add(new AssistantMessage(fullResponse)); // getOutput() вернёт Message (AssistantMessage)

        // Парсим JSON из ответа (если модель вернула JSON). Предположим, что модель всегда включает JSON workflow.
        String workflowJson = extractJsonFromText(fullResponse);
        LLMRespDto result = new LLMRespDto();
        if (workflowJson != null) {
            result.setWorkflowJson(workflowJson);
            try {
                // Обновляем текущее workflow для сессии
                WorkflowDefinitionDto workflow = new ObjectMapper().readValue(workflowJson, WorkflowDefinitionDto.class);
                currentWorkflows.put(sessionId, workflow);
            } catch (Exception e) {
                // Если вдруг JSON не парсится, отмечаем ошибку, но все равно возвращаем текст
                result.setWorkflowJson("{}");
            }
        }
        // Ассистент мог вернуть пояснения или вопросы вместе с JSON
        result.setAssistantMessage(stripJsonFromText(fullResponse));
        return result;
    }

    // Формирование system-instruction с учетом текущего состояния workflow и справочника
    private Message createSystemInstruction(String sessionId) {
        StringBuilder sysText = new StringBuilder();
        sysText.append("Ты – интеллектуальный помощник для построения интеграционных workflow.\n")
                .append("Формат workflow – JSON в соответствии с предопределенным DSL (WorkflowDefinitionDto).\n")
                .append("Допустимые типы Starter: ").append(String.join(", ", knowledgeBase.getAllowedStarterTypes())).append(".\n")
                .append("Допустимые типы Activity: ").append(String.join(", ", knowledgeBase.getAllowedActivityTypes())).append(".\n")
                .append("Если пользователь описывает шаг, которого нет в этом списке, сообщи, что такой тип недоступен.\n")
                .append("Если не хватает данных (например, отсутствует обязательный параметр), задавай уточняющие вопросы.\n")
                .append("Никогда не выдумывай неуказанные значения — всегда уточни у пользователя.\n")
                .append("Выводи результат в формате JSON по схеме WorkflowDefinitionDto. Кроме JSON, можешь дать краткий комментарий пользователю.\n");
        // Если для данной сессии уже есть текущий workflow, включаем его в контекст
        if (currentWorkflows.containsKey(sessionId)) {
            sysText.append("Текущий Workflow JSON: ```").append(currentWorkflows.get(sessionId).toJson()).append("```\n")
                    .append("На основе текущего workflow выполни запрос пользователя.");
        }
        return new SystemMessage(sysText.toString());
    }

    // Вспомогательный метод: извлекает JSON (объект { ... }) из текста ответа
    private String extractJsonFromText(String text) {
        String startMarker = "```json";
        String endMarker = "```";

        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) return null;

        // Находим конец первого блока
        int jsonStart = startIndex + startMarker.length();
        int endIndex = text.indexOf(endMarker, jsonStart);
        if (endIndex == -1) return null;

        // Обрезаем и чистим JSON
        String json = text.substring(jsonStart, endIndex).trim();
        return json;
    }

    // Вспомогательный метод: убирает JSON из ответа, оставляя только комментарий/сообщение модели
    private String stripJsonFromText(String text) {
        // Если JSON обёрнут в кодовый блок или просто присутствует, вырезаем его
        String stripped = text;
        // Уберем все содержимое внутри тройных бэктиков ``` ``` для читаемости (предполагаем, что JSON внутри них)
        if (stripped.contains("```")) {
            stripped = stripped.replaceAll("(?s)```.*?```", "").trim();
        }
        // Также уберем JSON объект в фигурных скобках, если остался без бэктиков
        if (stripped.contains("{") && stripped.contains("}")) {
            stripped = stripped.replaceAll("(?s)\\{.*?\\}", "").trim();
        }
        return stripped;
    }
}
