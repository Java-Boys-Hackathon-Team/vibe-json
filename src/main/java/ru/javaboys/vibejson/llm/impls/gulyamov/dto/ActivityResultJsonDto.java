package ru.javaboys.vibejson.llm.impls.gulyamov.dto;

import lombok.Getter;

@Getter
public class ActivityResultJsonDto {
    private final String activity;
    private final String jsonActivity;

    public ActivityResultJsonDto(String activity, String jsonActivity) {
        this.activity = activity;
        this.jsonActivity = jsonActivity;
    }

    @Override
    public String toString() {
        return "ActivityResultDto{" +
                "activity='" + activity + '\'' +
                ", jsonActivity='" + jsonActivity + '\'' +
                '}';
    }
}
