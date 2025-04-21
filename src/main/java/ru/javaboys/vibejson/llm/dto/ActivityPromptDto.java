package ru.javaboys.vibejson.llm.dto;

public class ActivityPromptDto {
    private String activity;
    private String prompt;

    public ActivityPromptDto(String activity, String prompt) {
        this.activity = activity;
        this.prompt = prompt;
    }

    public String getActivity() {
        return activity;
    }

    public String getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "ActivityPromptDto{" +
                "activity='" + activity + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}
