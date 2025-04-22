package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Сообщение Kafka")
public class KafkaMessageDto {

    @NotBlank
    private Object payload;

}
