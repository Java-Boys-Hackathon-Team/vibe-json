package ru.javaboys.vibejson.llm.dto;

public class ActivityDto {
    private String activity;
    private String text;

    public ActivityDto() { }

    public ActivityDto(String activity, String text) {
        this.activity = activity;
        this.text = text;
    }

    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
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
