package com.example.blaybus4th.domain.object.repository;

import com.example.blaybus4th.domain.object.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findById(Long id);

    @Query("""
            select m
              from Model m
              join m.modelComponents c
             where m.modelId = :modelId
            """)
    Optional<Model> findWithComponent(Long modelId);

    @Query("""
            select distinct m
              from Model m
              left join fetch m.modelComponents
             where m.modelId <> :modelId
            """
    )
    List<Model> findAllExcludingCurrentId(Long modelId);
}
