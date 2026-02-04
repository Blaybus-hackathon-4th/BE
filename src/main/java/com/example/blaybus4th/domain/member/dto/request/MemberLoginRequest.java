package com.example.blaybus4th.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberLoginRequest {

    private String institutionId;

    private String verificationCode;

}
