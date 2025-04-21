package ru.javaboys.vibejson.llm.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.JsonDoc;
import ru.javaboys.vibejson.llm.dto.ActivityDto;
import ru.javaboys.vibejson.llm.dto.ActivityPromptDto;
import ru.javaboys.vibejson.llm.dto.ActivityResultJsonDto;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.llm.springai.ChatGPTService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LLMServiceChainFlowForge implements LLMService {

    private static final Logger log = LoggerFactory.getLogger(LLMServiceChainFlowForge.class);
    private final ChatGPTService chatGPTService;
    private final ObjectMapper mapper;
    private final Executor asyncExecutor;

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {
        try {
            // 0) Делаем текст более удобным для обработки
            prompt = parseWorkflowSteps(prompt);

            log.info("Обработанный промпт : {}", prompt);

            // 1) Получаем все активности из промта пользователя
            List<ActivityDto> activities = extractMentionsWithDocs(prompt);

            log.info("Активности : {}", activities);

            // сразу проверяем на “чужие” активности
            Optional<LLMResponseDto> unknownCheck = checkUnknownActivities(activities);

            log.info("Неизвестные активности : {}", activities);

            if (unknownCheck.isPresent()) {
                return unknownCheck.get();
            }

            // 3) Получаем список промтов для генерации каждой активности
            List<ActivityPromptDto> activityPrompts = buildPromptsForFoundActivities(activities);

            // 4) // асинхронно шлём все промты и ждём результата
            List<ActivityResultJsonDto> results = callAllActivityPromptsAsync(activityPrompts);

            String wfExample = IOUtils.toString(new ClassPathResource("wf-defenition/wf-1.json").getInputStream(), StandardCharsets.UTF_8);

            return LLMResponseDto.builder()
                    .workflow(assembleWorkflowJson(results, wfExample, prompt))
                    .LLMChatMsg("Отлично! Ваш JSON‑workflow успешно сгенерирован и отображён. Загляните в область схемы!")
                    .build();

        } catch (Exception e) {
            return LLMResponseDto.builder()
                    .exception(e)
                    .build();
        }
    }

    @Override
    public String getModelCode() {
        return "Chain Flow Forge";
    }

    //---------------------------------

    /**
     * Шаг 2. Спрашиваем LLM: «Найди в prompt упоминания activity и верни JSON‑объект {act: doc,…}»
     */
    private List<ActivityDto> extractMentionsWithDocs(String prompt) {
        StringBuilder docList = new StringBuilder("Доступные Activity и их описания:\n");
        JsonDoc.ACTIVITY_DESCRIPTION_LIST.forEach((act, desc) ->
                docList.append(" - ").append(act).append(": ").append(desc).append("\n")
        );

        String extPrompt = String.format("""
                        %s

                        Текст для анализа (может содержать несколько разных блоков активностей):
                        \"%s\"

                        Задача:
                        1) Найди в этом тексте **все** блоки, относящиеся к любому из перечисленных Activity.
                        2) Каждый блок — это кусок текста, начинающийся с имени activity (точно из списка) и продолжающийся до следующей пустой строки.
                        3) Если блок явно описывает некую активность, но её имени нет в списке, **придумай короткое уникальное имя** (например, `custom_1`, `custom_2` и т.п.) и включи его в ответ.
                        4) Верни **ТОЛЬКО** JSON‑массив, где каждый элемент — это объект с двумя полями:
                           • \"activity\" — имя activity (например \"rest_call\" или твой `custom_X`)  
                           • \"text\"     — весь текст блока, включая переносы строк и маркеры (•, –, {…})

                        Формат ответа:
                        [
                          {
                            "activity": "<activity1>",
                            "text": "<весь первый блок с переносами и маркерами>"
                          },
                          {
                            "activity": "<activity2>",
                            "text": "<весь второй блок…>"
                          },
                          …
                        ]

                        Никаких обёрток, комментариев или пояснений — только чистый JSON.
                        """,
                docList,
                prompt
        );

        String rawJson = sendLLMRequest(extPrompt);

        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

            return mapper.readValue(rawJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Не удалось распарсить JSON‑ответ на извлечение activity: " + rawJson, e);
        }
    }

    private String buildSingleActivityPrompt(
            String activity,
            String description) {

        return String.format("""
                         Описание, введённое пользователем для «%1$s»:
                          %2$s

                        Официальная документация по «%1$s»:
                          %3$s

                        Пример JSON‑фрагмента для «%1$s»:
                          %4$s

                         Задача:
                          На основе только **вышеуказанной документации** и **примера** сгенерируй ровно один корректный JSON‑объект:

                          {
                            "id":      "<уникальный UUID>",
                            "type":    "%1$s",
                            "details": { … параметры из примера/документации … }
                          }

                          **ВЕРНИ _ТОЛЬКО_ JSON** (никаких пояснений, списков или обёрток).
                        """,
                activity,         // %1$s
                description,      // %2$s
                JsonDoc.ACTIVITY_DOCUMENTATION_LIST.get(activity),    // %3$s
                JsonDoc.ACTIVITY_EXAMPLE_LIST.get(activity)       // %4$s
        );
    }

    private List<ActivityPromptDto> buildPromptsForFoundActivities(List<ActivityDto> activities) {
        return activities.stream()
                .map(dto -> new ActivityPromptDto(
                        dto.getActivity(),
                        buildSingleActivityPrompt(dto.getActivity(), dto.getText())
                ))
                .collect(Collectors.toList());
    }


    private List<ActivityResultJsonDto> callAllActivityPromptsAsync(List<ActivityPromptDto> prompts) {
        List<CompletableFuture<ActivityResultJsonDto>> futures = prompts.stream()
                .map(ap -> CompletableFuture.supplyAsync(() -> {
                    // 2) Получаем ответ
                    String rawJson = chatGPTService.sendPrompt(ap.getPrompt());

                    // 3) Упаковываем в DTO
                    return new ActivityResultJsonDto(ap.getActivity(), rawJson);
                }, asyncExecutor))
                .toList();

        // 4) Ждём всех и собираем
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    /**
     * Запрос к LLM, общий метод
     */
    private String sendLLMRequest(String prompt) {
        return chatGPTService.sendPrompt(prompt);
    }

    public String assembleWorkflowJson(List<ActivityResultJsonDto> activities, String exampleWorkflowJson, String userPrompt) {
        try {
            String activitiesJson = mapper.writeValueAsString(activities);
            String extPrompt = String.format(
                    "Пример полного workflow:\n```json\n%s\n```\n" +
                            "Пользовательский промпт:\n```%s```\n" +
                            "Список JSON активностей:\n```json\n%s\n```\n" +
                            "Задача: На основе этой информации и документации сгенерировать итоговый JSON объекта workflow. " +
                            "ВЕРНИ ТОЛЬКО ЧИСТЫЙ JSON БЕЗ КАКИХ‑ЛИБО КОДОВЫХ БЛОКОВ (```).",
                    exampleWorkflowJson, userPrompt, activitiesJson
            );

            String raw = sendLLMRequest(extPrompt);

            return raw
                    .replaceAll("(?m)^```(?:json)?\\s*", "")
                    .replaceAll("(?m)```$", "")
                    .trim();
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка при сборке промпта: " + e.getMessage(), e);
        }
    }

    public String parseWorkflowSteps(String prompt) {
        String extPrompt = """
                Ты — помощник, который умеет разбивать сценарий на упорядоченные шаги.
                На входе тебе даётся текст:
                """ + prompt + """
                Твоя задача:
                1. Разбить этот текст на логическе шаги — в порядке их упоминания.
                2. Для каждого шага вывести объект с полями: step (номер шага), title (краткий заголовок), description (текст шага).
                Верни только JSON-массив объектов StepDto.
                """;
        return sendLLMRequest(extPrompt);
    }

    private Optional<LLMResponseDto> checkUnknownActivities(List<ActivityDto> activities) {
        Set<String> known = JsonDoc.ACTIVITY_DESCRIPTION_LIST.keySet();

        // отбираем только неизвестные
        List<ActivityDto> unknowns = activities.stream()
                .filter(a -> !known.contains(a.getActivity()))
                .toList();

        if (!unknowns.isEmpty()) {
            // строим подробное сообщение
            String details = unknowns.stream()
                    .map(a -> String.format("%s (текст: \"%s\")",
                            a.getActivity(),
                            a.getText().replaceAll("\\s+", " ").trim()))
                    .collect(Collectors.joining(", "));

            String msg = "Обнаружены неизвестные активности: " + details +
                    ". Проверьте написание или уберите их и попробуйте снова.";

            return Optional.of(LLMResponseDto.builder()
                    .LLMChatMsg(msg)
                    .build());
        }
        return Optional.empty();
    }
}
