package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.util.Map;

@Data
@EntityDescription("Внедрение данных")
public class InjectDataDto {
    private Map<String, Object> data;
}
