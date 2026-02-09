package com.example.blaybus4th.domain.object.dto;

import com.example.blaybus4th.domain.object.entity.ModelComponents;
import com.example.blaybus4th.domain.quiz.dto.QuizResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

public class ObjectResponseDTO {

    @Getter
    @Builder
    public static class ObjectCardResponseDTO{
        Long objectId;
        String objectImageUrl;
        String objectNameKr;
        String objectNameEn;
        String objectcontent;
        List<String> objectTags;
    }

    @Getter
    @Builder
    public static class ObjectComponentResponseDTO{
        Long componentId;
        String componentNameKr;
        String componentNameEn;
        String componentContent;
        List<ElementResponseDTO> elements;
    }

    @Getter
    @Builder
    public static class ElementResponseDTO{
        String elementName;
        String elementContent;
    }

    /// /////////////

    @Getter
    @Builder
    public static class ObjectDetailsResponseDTO{
        Long objectId;
        String objectNameKr;
        String objectNameEn;
        ObjectDiscription discription;
        List<ModelDTO> models;
    }

    /// //////////////

    @Getter
    @Builder
    public static class ObjectDiscription{
        private String objectContent; // 세부설명
        private List<String> principle; // 공학적 원리 및 매커니즘
        private List<String> structuralAdvantages; // 구조적 이점
        private List<String> designConstraints; // 설계상 제한사항
    }


    @Getter
    @Builder
    public static class ModelDTO {
        private Long modelId;
        private String nameKr;
        private String nameEn;
        private String description;
        private String modelUrl;
        private TransformDTO transform;
        private CameraDTO camera;
    }

    @Getter
    @Builder
    public static class TransformDTO {
        private List<Float> position; // [x, y, z]
        private List<Float> rotation; // [x, y, z] in radians
        private List<Float> scale;    // [x, y, z]
    }

    @Getter
    @Builder
    public static class CameraDTO {
        private List<Float> position; // [x, y, z]
        private List<Float> target;   // [x, y, z]
        private Float fov;
    }
}
