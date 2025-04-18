package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class RabbitmqMessagePropertiesDto {

    private String contentType;

    private String contentEncoding;

    private Integer priority;

    private Map<String, Object> headers;

    private String replyTo;

    private String expiration;

    @Size(max = 255)
    private String messageId;

    @Size(max = 255)
    private String type;
}
