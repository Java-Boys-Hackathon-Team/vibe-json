package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Ссылка на SSL Kafka")
public class KafkaSslDefDto {

    @NotBlank
    @Size(max = 255)
    private String trustStoreType;

    @NotBlank
    @Size(max = 255)
    private String trustStoreCertificates;
}

