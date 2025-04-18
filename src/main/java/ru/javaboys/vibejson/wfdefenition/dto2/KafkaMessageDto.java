package ru.javaboys.vibejson.wfdefenition.dto2;


import lombok.Data;

import java.util.Map;

@Data
public class KafkaMessageDto {

    private Object payload;

    private Map<String, Object> headers;

    private String key;
}
