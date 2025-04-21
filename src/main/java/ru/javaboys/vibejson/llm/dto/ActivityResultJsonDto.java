package ru.javaboys.vibejson.llm.dto;

public class ActivityResultJsonDto {
    private final String activity;
    private final String jsonActivity;

    public ActivityResultJsonDto(String activity, String jsonActivity) {
        this.activity = activity;
        this.jsonActivity = jsonActivity;
    }

    public String getActivity() {
        return activity;
    }

    public String getJsonActivity() {
        return jsonActivity;
    }

    @Override
    public String toString() {
        return "ActivityResultDto{" +
                "activity='" + activity + '\'' +
                ", jsonActivity='" + jsonActivity + '\'' +
                '}';
    }
}
