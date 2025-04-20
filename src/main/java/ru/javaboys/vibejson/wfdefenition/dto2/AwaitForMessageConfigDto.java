package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Конфигурация ожидания сообщения")
public class AwaitForMessageConfigDto {

    @NotBlank
    private String messageName;
}
