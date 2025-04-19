package ru.javaboys.vibejson.llm.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionRequestDTO {

    private String model;
    private List<MessageDTO> messages;
    private BigDecimal temperature;

}
