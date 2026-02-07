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
}
