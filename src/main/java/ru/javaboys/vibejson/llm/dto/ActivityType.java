package ru.javaboys.vibejson.llm.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum ActivityType {

    WORKFLOW_CALL("workflow", List.of("workflowRef")),
    AWAIT_FOR_MESSAGE("await_for_message", List.of("messageText")),
    REST_CALL("rest_call", List.of("url", "method")),
    DB_CALL("db_call", List.of("query")),
    SEND_TO_RABBITMQ("send_to_rabbitmq", List.of("queue", "payload")),
    SEND_TO_SAP("send_to_sap", List.of("idocType", "content")),
    XSLT_TRANSFORM("xslt_transform", List.of("xsltTemplate", "input")),
    INJECT("inject", List.of("data")),
    SWITCH("switch", List.of("conditions")),
    TIMER("timer", List.of("duration")),
    TRANSFORM("transform", List.of("mode", "source")),
    PARALLEL("parallel", List.of("branches"));

    private final String type;
    private final List<String> requiredParams;

    ActivityType(String type, List<String> requiredParams) {
        this.type = type;
        this.requiredParams = requiredParams;
    }

    public static Optional<ActivityType> fromType(String type) {
        return Arrays.stream(values())
                .filter(e -> e.getType().equalsIgnoreCase(type))
                .findFirst();
    }
}
