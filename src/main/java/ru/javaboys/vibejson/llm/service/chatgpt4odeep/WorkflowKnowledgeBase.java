package ru.javaboys.vibejson.llm.service.chatgpt4odeep;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import ru.javaboys.vibejson.wfdefenition.dto2.ActivityDto;
import ru.javaboys.vibejson.wfdefenition.dto2.StarterDto;
import ru.javaboys.vibejson.wfdefenition.dto2.WorkflowDefinitionDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WorkflowKnowledgeBase {

    /** Получить допустимые типы активити (строковые значения) */
    @Tool(description = "Получить допустимые типы activities (активити)")
    public List<String> getAllowedActivityTypes() {
        return Arrays.stream(ActivityType.values())
                .map(ActivityType::getType)
                .collect(Collectors.toList());
    }

    /** Получить справочное описание типов активити (для вывода в prompt GPT) */
    public String describeAllowedActivityTypes() {
        return Arrays.stream(ActivityType.values())
                .map(t -> "- `" + t.getType() + "`" +
                        (t.getRequiredParams().isEmpty() ? "" : " (required: " + String.join(", ", t.getRequiredParams()) + ")"))
                .collect(Collectors.joining("\n"));
    }

    /** Получить допустимые типы стартеров (например, KafkaConsumer, Scheduler и т.п.) */
    @Tool(description = "Получить допустимые типы starters (стартеров)")
    public List<String> getAllowedStarterTypes() {
        return Arrays.stream(StarterDto.class.getDeclaredFields())
                .filter(f -> f.getType().getSimpleName().endsWith("Dto"))
                .map(Field::getName)
                .map(this::toTypeName)
                .collect(Collectors.toList());
    }

    /** Валидация workflow: типы и обязательные параметры */
    @Tool(description = "Метод позволяет выполнить валидацию workflow")
    public ValidationResult validateWorkflow(WorkflowDefinitionDto workflow) {
        ValidationResult result = new ValidationResult();

        if (workflow.getDetails() == null) {
            result.addError("Missing workflow 'details' section.");
            return result;
        }

        // Стартеры
        List<StarterDto> starters = workflow.getDetails().getStarters();
        if (starters == null || starters.isEmpty()) {
            result.addError("No starters defined.");
        } else {
            for (StarterDto starter : starters) {
                validateStarter(starter, result);
            }
        }

        // Активности
        if (workflow.getCompiled() == null || workflow.getCompiled().getActivities() == null) {
            result.addError("Workflow 'compiled.activities' section is missing.");
        } else {
            for (ActivityDto activity : workflow.getCompiled().getActivities()) {
                validateActivity(activity, result);
            }
        }

        return result;
    }

    /** Проверка стартеров */
    private void validateStarter(StarterDto starter, ValidationResult result) {
        String type = starter.getType();
        if (type == null || type.isBlank()) {
            result.addError("Starter with missing 'type'.");
            return;
        }

        if (!getAllowedStarterTypes().contains(type)) {
            result.addError("Unknown starter type: '" + type + "'");
            return;
        }

        // Проверка, что соответствующий DTO внутри не пустой
        try {
            String fieldName = decapitalize(type); // KafkaConsumer -> kafkaConsumer
            Field field = StarterDto.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(starter);
            if (fieldValue == null) {
                result.addError("Starter type '" + type + "' is defined but missing inner DTO '" + fieldName + "'.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            result.addError("Starter type '" + type + "' has no matching DTO field.");
        }
    }

    /** Проверка активити */
    private void validateActivity(ActivityDto activity, ValidationResult result) {
        String type = activity.getType();
        String id = activity.getId() != null ? activity.getId() : "[no id]";

        if (type == null || type.isBlank()) {
            result.addError("Activity '" + id + "' has missing type.");
            return;
        }

        Optional<ActivityType> activityTypeOpt = ActivityType.fromType(type);
        if (activityTypeOpt.isEmpty()) {
            result.addError("Unknown activity type: '" + type + "' in activity '" + id + "'");
            return;
        }

        Map<String, Object> injectData = activity.getInjectData();
        for (String param : activityTypeOpt.get().getRequiredParams()) {
            if (injectData == null || !injectData.containsKey(param)) {
                result.addError("Activity '" + id + "' (" + type + ") missing required param: '" + param + "'");
            }
        }
    }

    // Преобразование camelCase -> PascalCase
    private String toTypeName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) return fieldName;
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    // Преобразование PascalCase -> camelCase
    private String decapitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }
}

