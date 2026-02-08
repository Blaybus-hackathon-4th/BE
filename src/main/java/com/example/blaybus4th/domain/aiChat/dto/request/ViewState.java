package com.example.blaybus4th.domain.aiChat.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ViewState {

    private Double explosionValue; // 부품 분해 정도
    private List<Double> cameraPosition; // 카메라 위치


}
