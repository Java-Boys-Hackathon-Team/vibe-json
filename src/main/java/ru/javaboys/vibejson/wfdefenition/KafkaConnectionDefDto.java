package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Определение подключения Kafka")
public class KafkaConnectionDefDto {

    @NotBlank
    @Size(max = 255)
    private String bootstrapServers;

    @Valid
    @NotNull
    private KafkaAuthDefDto authDef;
}
