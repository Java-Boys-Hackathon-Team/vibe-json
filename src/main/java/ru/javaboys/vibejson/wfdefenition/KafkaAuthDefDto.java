package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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