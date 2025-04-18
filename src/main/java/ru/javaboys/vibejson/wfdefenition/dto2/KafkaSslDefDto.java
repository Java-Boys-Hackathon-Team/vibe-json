package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KafkaSslDefDto {

    @NotBlank
    private String trustStoreType;

    @NotBlank
    private String trustStoreCertificates;
}

