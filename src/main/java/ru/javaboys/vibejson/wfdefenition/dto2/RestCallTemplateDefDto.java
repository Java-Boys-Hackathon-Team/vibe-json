package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.Map;

@Data
public class RestCallTemplateDefDto {

    private String curl; // если используется "сырой" curl

    private String method; // GET, POST, PUT и т.д.

    private String url;

    private String bodyTemplate;

    private Map<String, String> headers;

    @Valid
    private RestAuthDefDto authDef;
}
