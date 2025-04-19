package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendToS3ConfigDto {

    @NotNull
    @Valid
    private S3ConnectionRefDto connectionRef;

    @NotNull
    @Valid
    private S3ConnectionDefDto connectionDef;

    @NotBlank
    @Size(max = 255)
    private String bucket;

    @NotBlank
    @Size(max = 255)
    private String region;

    @NotNull
    @Valid
    private S3FileDto s3File;

}
