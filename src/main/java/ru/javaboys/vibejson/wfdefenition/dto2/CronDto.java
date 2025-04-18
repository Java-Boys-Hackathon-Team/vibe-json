package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;

@Data
public class CronDto {

    // хотя бы один из них должен быть указан, но валидация будет на уровне сервиса

    private String dayOfWeek;

    private String dayOfMonth;

    private String month;

    private String hour;

    private String minute;
}
