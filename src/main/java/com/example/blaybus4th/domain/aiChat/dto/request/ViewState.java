package com.example.blaybus4th.domain.aiChat.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ViewState {

    private String currentModel; // 작업 중인 오브젝트 이름
    private String selectedPartId; // 선택한 부품 ID
    private Double explosionValue; // 부품 분해 정도
    private List<Double> cameraPosition;


}
