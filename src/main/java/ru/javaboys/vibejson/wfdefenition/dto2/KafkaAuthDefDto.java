package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KafkaAuthDefDto {

    @NotBlank
    private String type; // SASL или TLS

    @Valid
    private KafkaSaslDto sasl;

    @Valid
    private KafkaTlsDto tls;
}