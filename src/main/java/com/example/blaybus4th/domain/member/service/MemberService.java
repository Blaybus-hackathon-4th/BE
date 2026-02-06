package com.example.blaybus4th.domain.member.service;

import com.example.blaybus4th.domain.member.dto.request.MemberLoginRequest;
import com.example.blaybus4th.domain.member.dto.response.InstitutionsListResponse;
import com.example.blaybus4th.domain.member.entity.Institution;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.InstitutionRepository;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import com.example.blaybus4th.global.security.JwtTokenProvider;
import com.example.blaybus4th.global.security.service.JwtCookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final InstitutionRepository institutionRepository;


    @Transactional
    public String memberLogin(MemberLoginRequest request) {

        Member member = memberRepository
                .findByInstitutionAndVerificationCode(
                        Long.parseLong(request.getInstitutionId()),
                        request.getVerificationCode()
                )
                .orElseThrow(() ->
                        new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND)
                );

        return jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
    }


    @Transactional
    public List<InstitutionsListResponse> getInstitutionsList() {
        List<Institution> result = institutionRepository.findAll();
        return result.stream()
                .map(InstitutionsListResponse::from)
                .toList();

    }
}
