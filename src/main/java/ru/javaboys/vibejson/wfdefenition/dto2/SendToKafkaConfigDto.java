package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SendToKafkaConfigDto {

    @NotBlank
    private String topic;

    private String key;

    @NotNull
    @Valid
    private KafkaMessageDto message;

    @NotNull
    @Valid
    private KafkaConnectionDefDto connectionDef;
}
