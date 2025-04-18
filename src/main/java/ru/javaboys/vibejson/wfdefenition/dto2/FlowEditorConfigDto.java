package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;

import java.util.Map;


@Data
public class FlowEditorConfigDto {

    private Map<String, Object> startMetadata;

    private Map<String, Object> activityMetadata;

    private Boolean horizontalLayout;
}
