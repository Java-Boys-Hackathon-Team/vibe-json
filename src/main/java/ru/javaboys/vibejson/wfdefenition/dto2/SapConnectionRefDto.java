package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Ссылка на подключение SAP")
public class SapConnectionRefDto {

    private UUID id;

    @Size(max = 255)
    private String name;

    private Integer version;

    @Size(max = 255)
    private String tenantId;

}
