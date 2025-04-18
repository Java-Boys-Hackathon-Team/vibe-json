package ru.javaboys.vibejson.llm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.javaboys.vibejson.llm.dto.GPTModelsDTO;
import ru.javaboys.vibejson.llm.service.GPTService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;

    @GetMapping(value = "/listModels", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GPTModelsDTO getModels() {
        return gptService.getGPTModels();
    }


}
