package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255)
    private String trustStoreType;
}
