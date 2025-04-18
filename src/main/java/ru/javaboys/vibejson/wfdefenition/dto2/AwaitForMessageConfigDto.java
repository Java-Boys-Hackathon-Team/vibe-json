package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AwaitForMessageConfigDto {

    @NotBlank
    private String messageName;
}
