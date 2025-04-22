package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Определение входящего SAP")
public class SapInboundDefDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Valid
    private SapConnectionDefDto connectionDef;

    @Valid
    private Map<String, String> props;
}

