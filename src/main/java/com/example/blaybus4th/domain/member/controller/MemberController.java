package com.example.blaybus4th.domain.member.controller;

import com.example.blaybus4th.domain.member.dto.request.MemberLoginRequest;
import com.example.blaybus4th.domain.member.dto.response.InstitutionsListResponse;
import com.example.blaybus4th.domain.member.service.MemberService;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import com.example.blaybus4th.global.security.service.JwtCookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtCookie jwtCookie;

    /**
     * 회원 로그인 api
     * 담당자 : 김영욱
     */
    @PostMapping("/login")
    public ApiResponse<String> memberLogin(@RequestBody MemberLoginRequest request, HttpServletResponse response){
        log.debug("요청 정보 = {}", request);
        String accessToken = memberService.memberLogin(request);
        jwtCookie.setAccessToken(response,accessToken);
        return ApiResponse.onSuccess("로그인 성공");
    }

    /**
     * 기관 목록 조회
     * 담당자 : 김영욱
     */
    @GetMapping("/institutions")
    public ApiResponse<List<InstitutionsListResponse>> getInstitutionsList(){
        return ApiResponse.onSuccess(memberService.getInstitutionsList());
    }


}
