package com.example.blaybus4th.domain.object.converter;

import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
import com.example.blaybus4th.domain.object.entity.Model;
import com.example.blaybus4th.domain.object.entity.ModelComponents;
import com.example.blaybus4th.domain.object.entity.Object;

import java.util.List;

public class ObjectConverter {

    public static ObjectResponseDTO.ObjectCardResponseDTO toObjectCardResponseDTO(
            Object object, List<String> tags){
        return ObjectResponseDTO.ObjectCardResponseDTO.builder()
                .objectImageUrl(object.getObjectThumbnail())
                .objectTags(tags)
                .objectNameKr(object.getObjectNameKr())
                .objectNameEn(object.getObjectNameEn())
                .objectcontent(object.getObjectDescription())
                .objectId(object.getObjectId())
                .build();
    }

    public static ObjectResponseDTO.ObjectComponentResponseDTO toObjectComponentResponseDTO(Model model, List<ObjectResponseDTO.ElementResponseDTO> element){
        return ObjectResponseDTO.ObjectComponentResponseDTO.builder()
                .componentId(model.getModelId())
                .componentNameKr(model.getModelNameKr())
                .componentNameEn(model.getModelNameEn())
                .componentContent(model.getModelContent())
                .elements(element)
                .build();
    }

    public static ObjectResponseDTO.ElementResponseDTO toElementResponseDTO(ModelComponents modelComponents){
        return ObjectResponseDTO.ElementResponseDTO.builder()
                .elementName(modelComponents.getModelComponentsName())
                .elementContent(modelComponents.getModelComponentsContent())
                .build();
    }

    public static ObjectResponseDTO.ModelDTO toModelDTO(
            Model model,
            ObjectResponseDTO.TransformDTO transform,
            ObjectResponseDTO.CameraDTO cameraDTO
            ){
        return ObjectResponseDTO.ModelDTO.builder()
                .modelId(model.getModelId())
                .nameKr(model.getModelNameKr())
                .nameEn(model.getModelNameEn())
                .description(model.getModelContent())
                .modelUrl(model.getModelURL())
                .transform(transform)
                .camera(cameraDTO)
                .build();
    }

    public static ObjectResponseDTO.TransformDTO toTransformDTO(Model model) {
        return ObjectResponseDTO.TransformDTO.builder()
                .position(List.of(model.getPosX(), model.getPosY(), model.getPosZ()))
                .rotation(List.of(model.getRotX(), model.getRotY(), model.getRotZ())) // 필요시 rotW 포함 여부 결정
                .scale(List.of(model.getScaleX(), model.getScaleY(), model.getScaleZ()))
                .build();
    }

    public static ObjectResponseDTO.CameraDTO toCameraDTO(Model model) {
        return ObjectResponseDTO.CameraDTO.builder()
                .position(List.of(model.getCamPosX(), model.getCamPosY(), model.getCamPosZ()))
                .target(List.of(model.getCamTargetX(), model.getCamTargetY(), model.getCamTargetZ()))
                .fov(model.getCamFov())
                .build();
    }

    public static ObjectResponseDTO.ObjectDiscription toObjectDiscription(
            String objectDiscription,
            List<String> principle,
            List<String> designConstraints,
            List<String> structuralAdvantages){
        return ObjectResponseDTO.ObjectDiscription.builder()
                .objectContent(objectDiscription)
                .principle(principle)
                .structuralAdvantages(structuralAdvantages)
                .designConstraints(designConstraints)
                .build();
    }

    public static ObjectResponseDTO.ObjectDetailsResponseDTO toObjectDetailsResponseDTO(
            Object object,
            ObjectResponseDTO.ObjectDiscription discription,
            List<ObjectResponseDTO.ModelDTO> models
    ){
        return ObjectResponseDTO.ObjectDetailsResponseDTO.builder()
                .objectId(object.getObjectId())
                .objectNameKr(object.getObjectNameKr())
                .objectNameEn(object.getObjectNameEn())
                .discription(discription)
                .models(models)
                .build();
    }
}
