package com.example.blaybus4th.domain.member.service;

import com.example.blaybus4th.domain.member.dto.request.MemberLoginRequest;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.global.security.JwtTokenProvider;
import com.example.blaybus4th.global.security.service.JwtCookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookie jwtCookie;

    @Transactional
    public String memberLogin(MemberLoginRequest request) {

        Member member = memberRepository
                .findByInstitutionAndVerificationCode(
                        Long.parseLong(request.getInstitutionId()),
                        request.getVerificationCode()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid institution or verification code")
                );

        return jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
    }



}
