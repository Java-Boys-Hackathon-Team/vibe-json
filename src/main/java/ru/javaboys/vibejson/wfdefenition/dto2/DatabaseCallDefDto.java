package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DatabaseCallDefDto {

    @Size(max = 255)
    private String type;

    @NotBlank
    private String sql;

    @Size(max = 255)
    private String schema;

    @Size(max = 255)
    private String catalog;

    @Size(max = 255)
    private String functionName;

    private Map<String, String> inParameters;

    private Map<String, String> outParameters;
}
