package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class XsltTransformConfigDto {

    /**
     * XSLT-шаблон (или ссылка на переменную вида jp{...})
     */
    @NotBlank
    private String xslt;

    /**
     * Исходный XML-документ
     */
    @NotBlank
    private String xml;

    /**
     * Имя переменной, в которую записать результат трансформации
     */
    @NotBlank
    private String resultVar;
}
