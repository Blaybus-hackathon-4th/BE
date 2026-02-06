package com.example.blaybus4th.domain.object.converter;

import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
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
}
