package com.example.blaybus4th.domain.object.controller;

import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import com.example.blaybus4th.domain.object.service.ObjectQueryService;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/objects")
public class ObjectController {

    private final ObjectQueryService objectQueryService;

    /**
     * 오브젝트 전체 목록 조회 api
     * 담당자 : 이소정
     */
    @GetMapping
    public ApiResponse<List<ObjectResponseDTO.ObjectCardResponseDTO>> getObjectList(
            @RequestParam(required = false) ObjectCategory category
    ){
        return ApiResponse.onSuccess(objectQueryService.getObjectLists(category));
    }

    /**
     * 오브젝트 선택 조회 api
     * 담당자 : 이소정
     */
    @GetMapping("/by-ids")
    public ApiResponse<List<ObjectResponseDTO.ObjectCardResponseDTO>> getObjectsByIds(
            @RequestParam List<Long> ids
    ){
        return ApiResponse.onSuccess(objectQueryService.getObjectsByIds(ids));
    }

    /**
     * 오브젝트 부품 개별 정보 조회 api
     * 담당자 : 이소정
     */
    @GetMapping("/components/{componentId}")
    public ApiResponse<ObjectResponseDTO.ObjectComponentResponseDTO> getComponent(
            @PathVariable Long componentId
    ){
        ObjectResponseDTO.ObjectComponentResponseDTO result = objectQueryService.getObjectComponentResponseDTO(componentId);
        return ApiResponse.onSuccess(result);
    }

    /**
     * 오브젝트 키워드 검색 api
     * 담당자 : 이소정
     */
    @GetMapping("/search")
    public ApiResponse<List<ObjectResponseDTO.ObjectCardResponseDTO>> searchObject(
            @RequestParam String keyword
    ){
        return ApiResponse.onSuccess(objectQueryService.searchObjects(keyword));
    }
}
