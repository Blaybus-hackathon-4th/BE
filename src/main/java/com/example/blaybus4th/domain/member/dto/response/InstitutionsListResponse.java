package com.example.blaybus4th.domain.member.dto.response;


import com.example.blaybus4th.domain.member.entity.Institution;

public record InstitutionsListResponse(
        Long institutionId,
        String institutionName
) {

    public static InstitutionsListResponse from(Institution institution) {
        return new InstitutionsListResponse(
                institution.getInstitutionId(),
                institution.getInstitutionName()
        );
    }


}
