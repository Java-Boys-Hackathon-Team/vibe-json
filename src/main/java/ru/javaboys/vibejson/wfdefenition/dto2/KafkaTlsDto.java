package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KafkaTlsDto {

    @NotBlank
    private String keyStoreCertificates;

    @NotBlank
    private String keyStoreKey;

    @NotBlank
    private String trustStoreCertificates;

    @NotBlank
    private String trustStoreType;
}
