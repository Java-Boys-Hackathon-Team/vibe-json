package ru.javaboys.vibejson.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import ru.javaboys.vibejson.converter.JsonbConverter;

@JmixEntity
@Table(name = "WORKFLOW_CONFIGURATION")
@Entity
public class WorkflowConfiguration {

    @Id
    @Column(name = "JSON_PATH", nullable = false, columnDefinition = "text")
    @Lob
    private String jsonPath;

    @Convert(converter = JsonbConverter.class)
    @Column(name = "JSON", columnDefinition = "jsonb")
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

}