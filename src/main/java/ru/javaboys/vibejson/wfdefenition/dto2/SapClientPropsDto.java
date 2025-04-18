package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SapClientPropsDto {

    @NotBlank
    private String jcoClientLang;

    @NotBlank
    private String jcoClientPasswd;

    @NotBlank
    private String jcoClientUser;

    @NotBlank
    private String jcoClientAshost;

    @NotNull
    private Integer jcoClientSysnr;

    @NotNull
    private Integer jcoClientClient;

    @NotNull
    private Integer jcoDestinationPoolCapacity;

    @NotNull
    private Integer jcoDestinationPeakLimit;
}
