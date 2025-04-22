package ru.javaboys.vibejson.llm.impls.werder.mwsgpt.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.javaboys.vibejson.llm.impls.werder.mwsgpt.dto.ChatCompletionRequestDTO;
import ru.javaboys.vibejson.llm.impls.werder.mwsgpt.dto.ChatCompletionResponseDTO;
import ru.javaboys.vibejson.llm.impls.werder.mwsgpt.dto.GPTModelListDTO;

@Component
public class GPTMTSClient {

    @Qualifier("mwsWebClient")
    private final WebClient mwsWebClient;

    public GPTMTSClient(WebClient mwsWebClient) {
        this.mwsWebClient = mwsWebClient;
    }

    public GPTModelListDTO listModels() {
        return mwsWebClient
                .get()
                .uri("/v1/models")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalStateException(
                                        "Ошибка MWS: " + resp.statusCode() + " – " + body)))
                .bodyToMono(GPTModelListDTO.class)
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
