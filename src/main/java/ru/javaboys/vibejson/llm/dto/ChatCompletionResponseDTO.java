package ru.javaboys.vibejson.llm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatCompletionResponseDTO {

    private String id;
    private Long   created;
    private String model;
    private String object;
    private String systemFingerprint;
    private List<ChoiceDTO> choices;
    private UsageDTO usage;
    private String  serviceTier;
    private Object  promptLogprobs;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChoiceDTO {
        private String      finishReason;
        private Integer     index;
        private MessageDTO  message;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MessageDTO {
        private String content;
        private String role;

        @JsonProperty("tool_calls")
        private Object toolCalls;

        @JsonProperty("function_call")
        private Object functionCall;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UsageDTO {
        private Integer completionTokens;
        private Integer promptTokens;
        private Integer totalTokens;
        private Object  completionTokensDetails;
        private Object  promptTokensDetails;
    }

}