package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;

@Data
public class WorkflowDetailsDto {

    private RestCallConfigDto restCallConfig;

    private SendToKafkaConfigDto sendToKafkaConfig;

    private SendToS3ConfigDto sendToS3Config;

    private SendToSapConfigDto sendToSapConfig;

    private TransformConfigDto transformConfig;

    private AwaitForMessageConfigDto awaitForMessageConfig;

    private DatabaseCallConfigDto databaseCallConfig;

    private XsltTransformConfigDto xsltTransformConfig;

    private InjectDataDto inject;

    // другие примитивы при необходимости
}
