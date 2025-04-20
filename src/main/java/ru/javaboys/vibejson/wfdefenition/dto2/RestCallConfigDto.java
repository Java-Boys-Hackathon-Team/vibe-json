package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Конфигурация REST вызова")
public class RestCallConfigDto {

    @NotNull
    @Valid
    private RestCallTemplateDefDto restCallTemplateDef;
}
