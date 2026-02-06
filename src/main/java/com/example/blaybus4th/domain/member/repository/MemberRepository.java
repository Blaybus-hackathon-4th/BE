package com.example.blaybus4th.domain.member.repository;

import com.example.blaybus4th.domain.member.dto.request.MemberLoginRequest;
import com.example.blaybus4th.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
            select m
              from Member m
             where m.institution.institutionId = :institution
               and m.verificationCode = :verificationCode
            """)
    Optional<Member> findByInstitutionAndVerificationCode(Long institution, String verificationCode);
}
