package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class SapConnectionDefDto {

    @NotBlank
    private String name;

    private Map<String, String> props;
}
