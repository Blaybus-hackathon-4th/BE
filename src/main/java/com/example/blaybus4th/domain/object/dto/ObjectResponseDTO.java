package com.example.blaybus4th.domain.object.dto;

import com.example.blaybus4th.domain.object.entity.ModelComponents;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
}
