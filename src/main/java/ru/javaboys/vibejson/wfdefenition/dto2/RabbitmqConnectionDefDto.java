package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RabbitmqConnectionDefDto {

    @NotBlank
    private String userName;

    @NotBlank
    private String userPass;

    @NotEmpty
    private List<@NotBlank String> addresses;

    @NotBlank
    private String virtualHost;
}
