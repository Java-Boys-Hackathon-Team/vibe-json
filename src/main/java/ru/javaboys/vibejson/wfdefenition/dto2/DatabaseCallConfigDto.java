package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;


@Data
public class DatabaseCallConfigDto {

    @NotNull
    @Valid
    private DatabaseConnectionDefDto connectionDef;

    @NotBlank
    private String sql;

    private Map<String, String> parameters;
}
