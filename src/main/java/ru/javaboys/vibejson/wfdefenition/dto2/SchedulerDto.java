package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class SchedulerDto {

    @NotBlank
    private String type; // "cron"

    @NotNull
    @Valid
    private CronDto cron;

    @NotNull
    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private Map<String, Object> outputTemplate;
}

