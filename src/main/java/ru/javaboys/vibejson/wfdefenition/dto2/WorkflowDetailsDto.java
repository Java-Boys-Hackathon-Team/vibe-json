package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;

import java.util.Map;

@Data
public class WorkflowDetailsDto {

    // Добавляем эти два поля:
    private Map<String, Object> inputValidateSchema;

    private Map<String, Object> outputValidateSchema;

    // Примитивы
    private RestCallConfigDto restCallConfig;

    private SendToKafkaConfigDto sendToKafkaConfig;

    private SendToS3ConfigDto sendToS3Config;

    private SendToSapConfigDto sendToSapConfig;

    private SendToRabbitmqConfigDto sendToRabbitmqConfig;

    private TransformConfigDto transformConfig;

    private AwaitForMessageConfigDto awaitForMessageConfig;

    private DatabaseCallConfigDto databaseCallConfig;

    private XsltTransformConfigDto xsltTransformConfig;

    private InjectDataDto inject;

    // другие примитивы при необходимости
}
