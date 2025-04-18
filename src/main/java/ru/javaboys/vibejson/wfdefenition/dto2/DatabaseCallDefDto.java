package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class DatabaseCallDefDto {

    @Size(max = 100)
    private String type;

    @NotNull
    @Valid
    private DatabaseConnectionDefDto connectionDef;

    @NotBlank
    private String sql;

    private Map<String, String> parameters;
}
