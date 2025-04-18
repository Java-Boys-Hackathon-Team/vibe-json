package ru.javaboys.vibejson.wfdefenition.dto2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class TransitionDto {

    private String next;

    public TransitionDto() {}

    public TransitionDto(String next) {
        this.next = next;
    }

    @JsonCreator
    public static TransitionDto fromValue(Object value) {
        if (value instanceof String s) {
            return new TransitionDto(s);
        } else if (value instanceof java.util.Map<?, ?> map && map.containsKey("next")) {
            Object next = map.get("next");
            return new TransitionDto(next != null ? next.toString() : null);
        }
        throw new IllegalArgumentException("Cannot deserialize TransitionDto from: " + value);
    }

    @JsonValue
    public Object toJson() {
        return next;
    }
}
