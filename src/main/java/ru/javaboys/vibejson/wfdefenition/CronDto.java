package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Cron")
public class CronDto {

    // хотя бы один из них должен быть указан, но валидация будет на уровне сервиса
    @Size(max = 255)
    private String dayOfWeek;

    @Size(max = 255)
    private String dayOfMonth;

    @Size(max = 255)
    private String month;

    @Size(max = 255)
    private String hour;

    @Size(max = 255)
    private String minute;
}
