package com.example.blaybus4th.domain.object.repository;

import com.example.blaybus4th.domain.object.entity.ObjectDetailDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ObjectDetailDescriptionRepository extends JpaRepository<ObjectDetailDescription, Long> {

    @Query("""
            select distinct o
              from ObjectDetailDescription o
              left join fetch o.operationPrinciples
              left join fetch o.structuralFeatures
             where o.object.objectId = :objectId
            """)
    ObjectDetailDescription findAllDetail(Long objectId);
}
