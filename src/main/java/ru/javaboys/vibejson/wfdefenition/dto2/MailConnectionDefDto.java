package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение подключения почты")
public class MailConnectionDefDto {

    @NotBlank
    @Size(max = 255)
    private String protocol; // обычно "imap"

    @NotBlank
    @Size(max = 255)
    private String host;

    @NotNull
    @Size(max = 255)
    private Integer port;

    @NotNull
    @Valid
    private MailAuthDto mailAuth;
}