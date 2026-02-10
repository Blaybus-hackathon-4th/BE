package com.example.blaybus4th.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuizRequestSTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelInteractionRequestDTO {

        private Long currentModelId;
        private List<Long> interactedPartId;
        private List<ChatMessageDTO> chatHistory;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDTO {
        private String role;
        private String content;
    }
}
