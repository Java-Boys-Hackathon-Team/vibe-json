package ru.javaboys.vibejson.llm.impls.gulyamov.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActivityDto {
    private String activity;
    private String text;

    public ActivityDto() { }

    public ActivityDto(String activity, String text) {
        this.activity = activity;
        this.text = text;
    }

    @Override
    public String toString() {
        return "ActivityBlock{" +
                "activity='" + activity + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
