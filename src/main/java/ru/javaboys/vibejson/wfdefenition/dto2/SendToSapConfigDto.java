package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendToSapConfigDto {

    @NotNull
    @Valid
    private SapConnectionDefDto connectionDef;

    @NotNull
    @Valid
    private IdocDto idoc;
}
