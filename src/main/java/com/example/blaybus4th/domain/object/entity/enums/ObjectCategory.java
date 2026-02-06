package com.example.blaybus4th.domain.object.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ObjectCategory {

    AUTOMOTIVE_ENGINEERING("자동차공학"),
    MECHANICAL_ENGINEERING("기계공학"),
    ROBOTICS_ENGINEERING("로봇공학"),
    BIOMEDICAL_ENGINEERING("의공학"),
    BIOTECHNOLOGY("생명공학"),
    AEROSPACE_ENGINEERING("항공우주"),
    ELECTRICAL_ELECTRONIC_ENGINEERING("전기전자"),
    ARCHITECTURE("건축학"),
    CIVIL_ENGINEERING("토목");

    private final String displayName;
}