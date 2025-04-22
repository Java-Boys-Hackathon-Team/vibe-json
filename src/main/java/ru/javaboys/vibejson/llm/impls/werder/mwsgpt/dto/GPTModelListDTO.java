package ru.javaboys.vibejson.llm.impls.werder.mwsgpt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GPTModelListDTO {
    private ArrayList<GPTModelDTO> data;
}
