package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SapServerPropsDto {

    @NotBlank
    private String jcoServerGwhost;

    @NotBlank
    private String jcoServerProgid;

    @NotBlank
    private String jcoServerGwserv;

    @NotNull
    private Integer jcoServerConnectionCount;
}
