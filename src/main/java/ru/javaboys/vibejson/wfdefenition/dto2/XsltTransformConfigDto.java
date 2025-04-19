package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class XsltTransformConfigDto {

    private XsltTemplateRefDto  xsltTemplateRef;

    private XsltTransformTargetRefDto xsltTransformTargetRef;

    @Size(max = 255)
    private String xsltTemplate;

    @Size(max = 255)
    private String xsltTransformTarget;

}
