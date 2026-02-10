package com.example.blaybus4th.domain.quiz.mapper;

import com.example.blaybus4th.domain.quiz.dto.QuizRequestSTO;
import com.example.blaybus4th.domain.quiz.dto.QuizResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class QuizPromptMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toModelContext(QuizResponseDTO.ObjectInfoDTO info) {
        return """
                objectName: %s
                objectContent: %s
                engineering_principle: %s
                structuralAdvantages: %s
                designConstraints: %s
                """.formatted(
                info.getObjectName(),
                info.getObjectContent(),
                info.getPrinciple(),
                info.getStructuralAdvantages(),
                info.getDesignConstraints()
        );
    }

    public static String toCandidateParts(QuizResponseDTO.ObjectInfoDTO info) {
        try {
            return objectMapper.writeValueAsString(info.getComponentInfos());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toChatHistory(List<QuizRequestSTO.ChatMessageDTO> history) {
        StringBuilder sb = new StringBuilder();
        for (QuizRequestSTO.ChatMessageDTO msg : history) {
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return sb.toString();
    }
}
