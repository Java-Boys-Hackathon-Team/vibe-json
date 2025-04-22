package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Определение подключения SAP")
public class SapConnectionDefDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    private Map<String, String> props;
}
