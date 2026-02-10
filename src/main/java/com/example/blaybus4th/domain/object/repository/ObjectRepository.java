package com.example.blaybus4th.domain.object.repository;

import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.domain.object.entity.ObjectDetailDescription;
import com.example.blaybus4th.domain.object.entity.enums.ObjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObjectRepository extends JpaRepository<Object, Long> {

    @Query("""
    select distinct o
    from Object o
    left join fetch o.objectTags ot
    left join fetch ot.tag
    where (:category is null or o.objectCategory = :category)
    """)
    List<Object> findAllWithTags(ObjectCategory category);

    @Query("""
    select distinct o
    from Object o
    left join fetch o.objectTags ot
    left join fetch ot.tag
    where o.objectId in :ids
""")
    List<Object> findAllWithTagsByIds(@Param("ids") List<Long> ids);

    @Query("""
    select distinct o
    from Object o
    left join fetch o.objectTags ot
    left join fetch ot.tag
    where lower(o.objectNameKr) like lower(concat('%', :keyword, '%'))
       or lower(o.objectNameEn) like lower(concat('%', :keyword, '%'))
""")
    List<Object> findByObjectNameKrOrEn(@Param("keyword") String keyword);

    @Query("""
                select o
                  from Object o
                  left join fetch o.detailDescriptions d
                  left join fetch d.operationPrinciples
                  left join fetch d.structuralFeatures
                 where o.objectId = :objectId
            """)
    Optional<Object> findWithAllDetails(Long objectId);

    @Query("""
              select distinct o
              from Object o
             where o.objectId <> :objectId
            """
    )
    List<Object> findAllExcludingCurrentId(Long objectId);
}
