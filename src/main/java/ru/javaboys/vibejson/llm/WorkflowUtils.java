package ru.javaboys.vibejson.llm;

import ru.javaboys.vibejson.llm.dto.ActivityType;
import ru.javaboys.vibejson.wfdefenition.StarterDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowUtils {
    public static List<String> getAllowedActivityTypes() {
        return Arrays.stream(ActivityType.values())
                .map(ActivityType::getType)
                .collect(Collectors.toList());
    }

    public static List<String> getAllowedStarterTypes() {
        return Arrays.stream(StarterDto.class.getDeclaredFields())
                .filter(f -> f.getType().getSimpleName().endsWith("Dto"))
                .map(Field::getName)
                .map(WorkflowUtils::toTypeName)
                .collect(Collectors.toList());
    }

    private static String toTypeName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) return fieldName;
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
