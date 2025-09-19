package ru.javaboys.vibejson.llm;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import ru.javaboys.vibejson.llm.dto.ValidationResult;
import ru.javaboys.vibejson.wfdefenition.root.WorkflowDefinitionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WorkflowTools {

    @Tool(description = "Метод позволяет выполнить валидацию workflow")
    public ValidationResult validateWorkflow(
             @ToolParam(description = "Текущий сформированный интеграционный бизнес-процесс") WorkflowDefinitionDto workflow
    ) {
        ValidationResult result = new ValidationResult();
        List<String> errors = new ArrayList<>();

        if (workflow == null) {
            result.setIsValid(false);
            errors.add("workflow не может быть null");
            result.setErrors(errors);
            logValidationErrors(errors);
            return result;
        }

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<WorkflowDefinitionDto>> violations = validator.validate(workflow);

        if (!violations.isEmpty()) {
            errors = violations.stream()
                    .map(v -> formatViolation(v))
                    .collect(Collectors.toList());
            result.setIsValid(false);
            result.setErrors(errors);
            logValidationErrors(errors);
        } else {
            result.setIsValid(true);
            result.setErrors(List.of());
        }

        return result;
    }

    private String formatViolation(ConstraintViolation<?> v) {
        String path = v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
        Object invalid = v.getInvalidValue();
        String invalidStr;
        try {
            invalidStr = invalid == null ? "null" : String.valueOf(invalid);
            if (invalidStr.length() > 500) {
                invalidStr = invalidStr.substring(0, 500) + "...";
            }
        } catch (Exception e) {
            invalidStr = "<unprintable>";
        }
        String message = v.getMessage();
        return String.format("%s: %s (значение: %s)", path, message, invalidStr);
    }

    private void logValidationErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) return;
        String block = "\n================= Ошибки валидации Workflow =================\n"
                + errors.stream().map(e -> " - " + e).collect(Collectors.joining("\n"))
                + "\n=============================================================";
        log.error(block);
    }
}

