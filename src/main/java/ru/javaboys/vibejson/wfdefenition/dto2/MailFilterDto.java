package ru.javaboys.vibejson.wfdefenition.dto2;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@EntityDescription("Фильтр почты")
public class MailFilterDto {

    private List<String> senders;

    private List<String> subjects;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime startMailDateTime;
}
