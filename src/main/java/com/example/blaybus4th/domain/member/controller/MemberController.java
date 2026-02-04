package com.example.blaybus4th.domain.member.controller;

import com.example.blaybus4th.domain.member.dto.request.MemberLoginRequest;
import com.example.blaybus4th.domain.member.service.MemberService;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import com.example.blaybus4th.global.security.service.JwtCookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtCookie jwtCookie;

    @PostMapping("/login")
    public ApiResponse<String> memberLogin(@RequestBody MemberLoginRequest request, HttpServletResponse response){
        String accessToken = memberService.memberLogin(request);
        jwtCookie.setAccessToken(response,accessToken);
        return ApiResponse.onSuccess("로그인 성공");
    }


}
