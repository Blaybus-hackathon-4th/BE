package com.example.blaybus4th.global.apiPayload.code;


import com.example.blaybus4th.global.apiPayload.dto.ErrorReasonDTO;

public interface BaseErrorCode {

    ErrorReasonDTO getReason();

    ErrorReasonDTO getReasonHttpStatus();
}
