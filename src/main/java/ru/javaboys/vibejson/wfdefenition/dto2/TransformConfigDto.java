package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class TransformConfigDto {

    @NotBlank
    private String type; // xml_to_json, json_to_xml, xslt и т.д.

    @NotNull
    private Map<String, String> target;
}
