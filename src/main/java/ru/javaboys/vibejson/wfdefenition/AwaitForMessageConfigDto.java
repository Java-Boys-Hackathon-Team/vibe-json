package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Конфигурация ожидания сообщения")
public class AwaitForMessageConfigDto {

    @NotBlank
    private String messageName;
}
