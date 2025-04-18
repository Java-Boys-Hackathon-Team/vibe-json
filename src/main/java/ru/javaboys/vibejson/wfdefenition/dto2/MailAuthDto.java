package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailAuthDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
