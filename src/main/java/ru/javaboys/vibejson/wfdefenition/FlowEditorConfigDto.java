package ru.javaboys.vibejson.wfdefenition;

import lombok.Data;

import java.util.Map;


@Data
@EntityDescription("Конфигурация редактора потока")
public class FlowEditorConfigDto {

    private Map<String, Object> startMetadata;

    private Map<String, Object> activityMetadata;

    private Boolean horizontalLayout;
}
