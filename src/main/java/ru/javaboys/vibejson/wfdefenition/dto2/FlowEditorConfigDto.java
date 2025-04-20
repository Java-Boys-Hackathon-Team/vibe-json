package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.util.Map;


@Data
@EntityDescription("Конфигурация редактора потока")
public class FlowEditorConfigDto {

    private Map<String, Object> startMetadata;

    private Map<String, Object> activityMetadata;

    private Boolean horizontalLayout;
}
