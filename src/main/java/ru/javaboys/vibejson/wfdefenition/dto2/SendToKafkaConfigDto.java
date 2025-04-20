package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;


@Data
@EntityDescription("Конфигурация отправки в Kafka")
public class SendToKafkaConfigDto {

    @NotNull
    @Valid
    private KafkaConnectionRefDto connectionRef;

    @NotNull
    @Valid
    private KafkaConnectionDefDto connectionDef;

    @NotBlank
    @Size(max = 255)
    private String topic;

    @NotBlank
    @Size(max = 255)
    private String key;

    @NotNull
    @Valid
    private KafkaMessageDto message;

}
