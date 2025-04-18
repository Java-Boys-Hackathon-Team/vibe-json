package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class S3AuthDefDto {

    @NotBlank
    private String type; // accessKey, IAM и др.

    @Valid
    private AccessKeyAuthDto accessKeyAuth;
}
