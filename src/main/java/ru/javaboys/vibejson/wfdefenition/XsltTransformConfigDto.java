package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Конфигурация преобразования XSLT")
public class XsltTransformConfigDto {

    private XsltTemplateRefDto  xsltTemplateRef;

    private XsltTransformTargetRefDto xsltTransformTargetRef;

    @Size(max = 255)
    private String xsltTemplate;

    @Size(max = 255)
    private String xsltTransformTarget;

}
