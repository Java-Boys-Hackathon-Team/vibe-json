package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KafkaSaslDto {

    @NotBlank
    @Size(max = 255)
    private String protocol; // SASL_SSL или SASL_PLAINTEXT

    @NotBlank
    @Size(max = 255)
    private String mechanism; // SCRAM-SHA-512, OAUTHBEARER и др.

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

    @Valid
    private KafkaSslDefDto sslDef;

    @Size(max = 255)
    private String tokenUrl; // для OAUTHBEARER
}
