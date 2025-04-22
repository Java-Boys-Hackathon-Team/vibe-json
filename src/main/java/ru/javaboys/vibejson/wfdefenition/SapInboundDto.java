package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@EntityDescription("Входящий SAP")
public class SapInboundDto {

    @NotNull
    @Valid
    private SapInboundDefDto inboundDef;
}
