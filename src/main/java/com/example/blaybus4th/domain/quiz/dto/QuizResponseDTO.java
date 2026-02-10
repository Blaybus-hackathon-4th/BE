package com.example.blaybus4th.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class QuizResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class createQuizDTO {
        private String type;
        private String targetPartId;
        private String question;
        private String hint;
        private String successMessage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ModelInfoDTO {
        private Long modelId;
        private String modelName;
        private String modelContent;
        private List<ModelComponentDTO> modelComponents;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ModelComponentDTO {
        private String componentName;
        private String componentContent;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ObjectInfoDTO {
        private String objectName; // 오브젝트 이름
        private String objectContent; // 세부설명
        private List<String> principle; // 공학적 원리 및 매커니즘
        private List<String> structuralAdvantages; // 구조적 이점
        private List<String> designConstraints; // 설계상 제한사항
        private List<ModelInfoDTO> componentInfos; // 부품 정보
    }
}
