package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DatabaseCallRefDto {

    private UUID id;

    @Size(max = 255)
    private String name;

    private Integer version;

    @Size(max = 255)
    private String tenantId;

}
