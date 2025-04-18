package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestCallConfigDto {

    @NotNull
    @Valid
    private RestCallTemplateDefDto restCallTemplateDef;
}
