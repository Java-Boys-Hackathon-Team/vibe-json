package ru.javaboys.vibejson.wfdefenition.dto2;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение рабочего процесса")
public class WorkflowDefinitionDto {

    @NotBlank
    @Size(max = 255)
    private String type = "complex"; // чаще всего "complex"

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 4000)
    private String description;

    private Integer version = 1; // по умолчанию

    private String tenantId = "default"; // по умолчанию

    @NotNull
    private DetailsDto details;

    private CompiledDto compiled;

//    private FlowEditorConfigDto flowEditorConfig;

    // Метод для вывода всего workflow в JSON (например, для ответа UI)
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("JSON Workflow serialization error", e);
        }
    }
}
