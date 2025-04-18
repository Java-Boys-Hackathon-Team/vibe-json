package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SapInboundDefDto {

    @NotBlank
    private String name;

    @Valid
    private SapConnectionDefDto connectionDef;

    @Valid
    private SapServerPropsDto props;
}

