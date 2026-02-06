package com.example.blaybus4th.domain.aiChat.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
public class AiServiceRegistry {

    private final ChatModel chatModel;

    public AiServiceRegistry(@Value("${OPENAI_API_KEY}") String apiKey) {
        this.chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-5-mini")
                .timeout(Duration.ofMinutes(2))
                .build();
    }

}
