package ru.javaboys.vibejson.llm.impls.werder.mwsgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GPTModelDTO {
    private String id;
    private String object;
    private int created;
    @JsonProperty("owned_by")
    private String ownedBy;
}
