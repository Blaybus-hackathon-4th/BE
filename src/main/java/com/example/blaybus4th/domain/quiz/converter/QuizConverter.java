package com.example.blaybus4th.domain.quiz.converter;

import com.example.blaybus4th.domain.object.entity.Model;
import com.example.blaybus4th.domain.object.entity.ModelComponents;
import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.domain.object.entity.ObjectDetailDescription;
import com.example.blaybus4th.domain.quiz.dto.QuizResponseDTO;

import java.util.List;

public class QuizConverter {

    public static QuizResponseDTO.ModelInfoDTO toModelInfoDTO(
            Model model, List<QuizResponseDTO.ModelComponentDTO> components) {
        return QuizResponseDTO.ModelInfoDTO.builder()
                .modelId(model.getModelId())
                .modelName(model.getModelNameKr())
                .modelContent(model.getModelContent())
                .modelComponents(components)
                .build();
    }

    public static QuizResponseDTO.ModelComponentDTO toModelComponentDTO(
            ModelComponents modelComponents){
        return QuizResponseDTO.ModelComponentDTO.builder()
                .componentName(modelComponents.getModelComponentsName())
                .componentContent(modelComponents.getModelComponentsContent())
                .build();
    }

    public static QuizResponseDTO.ObjectInfoDTO toObjectInfoDTO(
            Object object,
            ObjectDetailDescription objectDetailDescription,
            List<String> principle,
            List<String> designConstraints,
            List<String> structuralAdvantages,
            List<QuizResponseDTO.ModelInfoDTO> componentInfos // currentObject 부품
    ) {
        return QuizResponseDTO.ObjectInfoDTO.builder()
                .objectName(object.getObjectNameKr())
                .objectContent(objectDetailDescription.getObjectDetailDescription())
                .principle(principle)
                .structuralAdvantages(structuralAdvantages)
                .designConstraints(designConstraints)
                .componentInfos(componentInfos) // currentObject 부품 리스트
                .build();
    }

}
