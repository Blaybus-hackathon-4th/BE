package com.example.blaybus4th.global.apiPayload.code;

import com.example.blaybus4th.global.apiPayload.dto.ReasonDTO;

public interface BaseSuccessCode {

    ReasonDTO getReason();

    ReasonDTO getReasonHttpStatus();
}
