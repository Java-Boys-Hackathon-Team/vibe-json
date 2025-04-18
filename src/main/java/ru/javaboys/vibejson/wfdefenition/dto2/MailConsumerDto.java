package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MailConsumerDto {

    @NotNull
    @Valid
    private MailConnectionDefDto connectionDef;

    @NotNull
    @Valid
    private MailFilterDto mailFilter;
}
