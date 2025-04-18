package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class TransformConfigDto {

    @NotBlank
    private String type;

    private Map<String, Object> source;

    private Map<String, Object> target;
}
