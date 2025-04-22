package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Конфигурация преобразования")
public class TransformConfigDto {

    @NotBlank
    @Size(max = 255)
    private String type;

    private Map<String, Object> target;

}
