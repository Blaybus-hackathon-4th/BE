package com.example.blaybus4th.domain.object.dto;

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
}
