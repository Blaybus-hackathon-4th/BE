package com.example.blaybus4th.object.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StructuralFeature")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StructuralFeature {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "structural_feature_id")
    private Long structuralFeatureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_detail_id", nullable = false)
    private ObjectDetailDescription objectDetailDescription;

    @Column(nullable = false, name = "structural_feature_description")
    private String structuralFeatureDescription;

}
