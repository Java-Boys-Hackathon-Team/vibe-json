package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendToS3ConfigDto {

    @NotBlank
    private String bucket;

    private String region;

    @NotNull
    @Valid
    private S3FileDto s3File;

    @NotNull
    @Valid
    private S3ConnectionDefDto connectionDef;
}
