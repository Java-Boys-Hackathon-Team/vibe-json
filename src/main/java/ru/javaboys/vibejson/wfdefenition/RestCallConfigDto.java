package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@EntityDescription("Конфигурация REST вызова")
public class RestCallConfigDto {

    @NotNull
    @Valid
    private RestCallTemplateDefDto restCallTemplateDef;
}
