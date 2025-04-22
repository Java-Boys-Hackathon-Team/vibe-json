package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@EntityDescription("Скомпилировано")
public class CompiledDto {

    @NotBlank
    private String start;

    @NotNull
    private List<ActivityDto> activities;

    // JSON-фильтр для вывода (опционально)
    private Map<String, Object> outputTemplate;
}
