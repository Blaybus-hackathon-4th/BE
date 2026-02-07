package com.example.blaybus4th.domain.object.repository;

import com.example.blaybus4th.domain.object.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findById(Long id);
}
