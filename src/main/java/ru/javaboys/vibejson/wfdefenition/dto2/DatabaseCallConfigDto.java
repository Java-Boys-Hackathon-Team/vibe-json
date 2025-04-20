package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;


@Data
@EntityDescription("Конфигурация вызова базы данных")
public class DatabaseCallConfigDto {

    @Valid
    private DatabaseCallRefDto databaseCallRef;

    @Valid
    private DatabaseCallDefDto databaseCallDef;

    @Size(max = 255)
    private String dataSourceId;

    @Valid
    private DataSourceDefDto dataSourceDef;

}
