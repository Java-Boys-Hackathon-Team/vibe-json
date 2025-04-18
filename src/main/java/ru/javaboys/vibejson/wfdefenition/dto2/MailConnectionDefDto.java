package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailConnectionDefDto {

    @NotBlank
    private String protocol; // обычно "imap"

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    @NotNull
    @Valid
    private MailAuthDto mailAuth;
}