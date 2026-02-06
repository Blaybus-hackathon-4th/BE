package com.example.blaybus4th.domain.member.repository;

import com.example.blaybus4th.domain.member.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
}
