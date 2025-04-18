package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class S3FileDto {

    @NotBlank
    private String filePath;

    @NotBlank
    private String content;
}
