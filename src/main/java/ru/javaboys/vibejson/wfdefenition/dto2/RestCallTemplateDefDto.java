package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.util.Map;

@Data
@EntityDescription("Определение шаблона REST вызова")
public class RestCallTemplateDefDto {

    private String curl; // если используется "сырой" curl

    private String method; // GET, POST, PUT и т.д.

    private String url;

    private String bodyTemplate;

    private Map<String, String> headers;

    @Valid
    private RestAuthDefDto authDef;
}
