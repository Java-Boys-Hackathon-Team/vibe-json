package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Определение вызова базы данных")
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
