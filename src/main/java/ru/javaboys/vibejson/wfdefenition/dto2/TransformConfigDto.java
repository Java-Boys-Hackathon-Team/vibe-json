package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransformConfigDto {

    @NotBlank
    @Size(max = 255)
    private String type;

    private Map<String, Object> target;

}
