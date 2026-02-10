package com.example.blaybus4th.domain.object.controller;

import com.example.blaybus4th.domain.object.dto.ObjectResponseDTO;
import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import com.example.blaybus4th.domain.object.service.ObjectQueryService;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "오브젝트 전체 목록 조회",description = "메인 페이지에 표시되는 오브젝트들의 목록을 조회합니다.")
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
    @Operation(summary = "오브젝트 전체 목록 조회",description = "오브젝트 목록을 조회합니다.")
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
    @Operation(summary = "오브젝트 부품 개별 정보 조회",description = "오브젝트 부품의 개별 정보(설명, 구성요소)를 조회합니다.")
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
    @Operation(summary = "오브젝트 키워드 검색",description = "오브젝트를 검색합니다. 오브젝트의 국문명, 영문명 모두 검색이 가능합니다.")
    public ApiResponse<List<ObjectResponseDTO.ObjectCardResponseDTO>> searchObject(
            @RequestParam String keyword
    ){
        return ApiResponse.onSuccess(objectQueryService.searchObjects(keyword));
    }

    /**
     * 오브젝트 키워드 검색 api
     * 담당자 : 이소정
     */
    @GetMapping("/{objectId}/details")
    @Operation(summary = "오브젝트 상세 정보 조회",description = "오브젝트를 상세 조회합니다.")
    public ApiResponse<ObjectResponseDTO.ObjectDetailsResponseDTO> searchObject(
            @PathVariable Long objectId
    ){
        return ApiResponse.onSuccess(objectQueryService.getObjectDetails(objectId));
    }
}
