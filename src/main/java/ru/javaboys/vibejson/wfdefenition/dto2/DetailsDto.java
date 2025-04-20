package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.util.List;
import java.util.Map;

@Data
@EntityDescription("Детали")
public class DetailsDto {

    // JSON Schema для входных параметров (опционально)
    private Map<String, Object> inputValidateSchema;

    // JSON Schema для выходных параметров (опционально)
    private Map<String, Object> outputValidateSchema;

    // Список стартеров - обязательное поле
    @NotNull
    @NotEmpty
    @Valid
    private List<StarterDto> starters;

    // Примитивы для различных типов процессов
    private SendToKafkaConfigDto sendToKafkaConfig;
    private SendToS3ConfigDto sendToS3Config;
    private RestCallConfigDto restCallConfig;
    private XsltTransformConfigDto xsltTransformConfig;
    private DatabaseCallConfigDto databaseCallConfig;
    private SendToRabbitmqConfigDto sendToRabbitmqConfig;
    private AwaitForMessageConfigDto awaitForMessageConfig;
    private SendToSapConfigDto sendToSapConfig;
}
