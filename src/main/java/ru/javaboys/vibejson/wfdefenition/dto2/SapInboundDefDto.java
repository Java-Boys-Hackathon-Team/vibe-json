package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

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

