package ru.javaboys.vibejson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfiguration {

    public static final String LLM_EXECUTOR = "llmExecutor";

    /**
     * Потоки для запросов к LLM. Один пул на всё приложение: раньше каждый
     * открытый экран чата заводил собственный пул и никогда его не закрывал,
     * так что потоки копились с каждым новым посетителем.
     */
    @Bean(LLM_EXECUTOR)
    public ThreadPoolTaskExecutor llmExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("llm-");
        executor.initialize();
        return executor;
    }
}
