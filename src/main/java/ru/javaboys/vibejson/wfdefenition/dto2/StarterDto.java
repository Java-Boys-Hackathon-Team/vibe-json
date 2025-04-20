package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Стартер")
public class StarterDto {

    @NotBlank
    @Size(max = 255)
    private String type; // rest_call, kafka_consumer, sap_inbound, scheduler и т.д.

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    @Valid
    private KafkaConsumerDto kafkaConsumer;

    @Valid
    private RabbitmqConsumerDto rabbitmqConsumer;

    @Valid
    private SapInboundDto sapInbound;

    @Valid
    private SchedulerDto scheduler;

    @Valid
    private MailConsumerDto mailConsumer;
}
