package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KafkaConnectionDefDto {

    @NotBlank
    private String bootstrapServers;

    @Valid
    private KafkaAuthDefDto authDef;
}
