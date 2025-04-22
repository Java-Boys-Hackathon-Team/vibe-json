package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
