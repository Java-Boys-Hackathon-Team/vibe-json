package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Ожидание сообщения")
public class AwaitForMessageDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String transition;

    @NotBlank
    @Size(max = 255)
    private String type;

    @NotNull
    private WorkflowCallDto workflowCall;

}
