package ru.javaboys.vibejson.llm.impls.gulyamov.dto;

import lombok.Getter;

@Getter
public class ActivityPromptDto {
    private String activity;
    private String prompt;

    public ActivityPromptDto(String activity, String prompt) {
        this.activity = activity;
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "ActivityPromptDto{" +
                "activity='" + activity + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}
