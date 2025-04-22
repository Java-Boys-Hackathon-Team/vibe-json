package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@EntityDescription("Определение подключения RabbitMQ")
public class RabbitmqConnectionDefDto {

    @NotBlank
    @Size(max = 255)
    private String userName;

    @NotBlank
    @Size(max = 255)
    private String userPass;

    @Valid
    @NotEmpty
    private List<@NotBlank String> addresses;

    @NotBlank
    @Size(max = 255)
    private String virtualHost;
}
