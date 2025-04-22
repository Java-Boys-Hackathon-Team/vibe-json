package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Авторизация почты")
public class MailAuthDto {

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;
}
