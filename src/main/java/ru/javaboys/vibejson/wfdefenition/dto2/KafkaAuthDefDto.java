package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение авторизации Kafka")
public class KafkaAuthDefDto {

    @NotBlank
    @Size(max = 255)
    private String type; // SASL или TLS

    @Valid
    private KafkaSaslDto sasl;

    @Valid
    private KafkaTlsDto tls;
}