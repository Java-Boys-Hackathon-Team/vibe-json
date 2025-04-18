package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KafkaSaslDto {

    @NotBlank
    private String protocol; // SASL_SSL или SASL_PLAINTEXT

    @NotBlank
    private String mechanism; // SCRAM-SHA-512, OAUTHBEARER и др.

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Valid
    private KafkaSslDefDto sslDef;

    private String tokenUrl; // для OAUTHBEARER
}
