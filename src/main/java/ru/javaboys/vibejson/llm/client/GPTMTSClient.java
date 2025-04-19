package ru.javaboys.vibejson.llm.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.javaboys.vibejson.llm.dto.ChatCompletionRequestDTO;
import ru.javaboys.vibejson.llm.dto.ChatCompletionResponseDTO;
import ru.javaboys.vibejson.llm.dto.GPTModelsDTO;

@Component
@RequiredArgsConstructor
public class GPTMTSClient {

    private final WebClient mwsWebClient;

    public GPTModelsDTO listModels() {
        return mwsWebClient
                .get()
                .uri("/v1/models")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalStateException(
                                        "Ошибка MWS: " + resp.statusCode() + " – " + body)))
                .bodyToMono(GPTModelsDTO.class)
                .block();
    }

    public ChatCompletionResponseDTO getAnswer(ChatCompletionRequestDTO request) {
        return mwsWebClient
                .post()
                .uri("v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalStateException(
                                        "Ошибка MWS GPT: " + resp.statusCode() + " – " + body))
                )
                .bodyToMono(ChatCompletionResponseDTO.class)
                .block();
    }

}
