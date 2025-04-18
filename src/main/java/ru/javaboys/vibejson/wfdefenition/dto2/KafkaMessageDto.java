package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class KafkaMessageDto {

    @NotNull
    private Map<String, Object> payload;
}
