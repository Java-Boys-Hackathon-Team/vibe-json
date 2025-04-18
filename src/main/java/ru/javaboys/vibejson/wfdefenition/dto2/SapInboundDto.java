package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SapInboundDto {

    @NotNull
    @Valid
    private SapInboundDefDto inboundDef;
}
