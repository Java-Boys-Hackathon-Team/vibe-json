package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Конфигурация отправки в SAP")
public class SendToSapConfigDto {

    @NotNull
    @Valid
    private SapConnectionRefDto connectionRef;

    @NotNull
    @Valid
    private SapConnectionDefDto connectionDef;

    @NotNull
    @Valid
    private IdocDto idoc;
}
