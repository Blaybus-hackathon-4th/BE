package com.example.blaybus4th.domain.object.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ObjectDetailDescription")
@Getter
public class ObjectDetailDescription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "object_detail_id")
    private Long objectDetailId;

    @JoinColumn(name = "object_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Object object;

    @Column(nullable = false,name = "object_detail_description")
    private String objectDetailDescription;

    @OneToMany(mappedBy = "objectDetailDescription", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OperationPrinciple> operationPrinciples = new HashSet<>();

    @OneToMany(mappedBy = "objectDetailDescription", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<StructuralFeature> structuralFeatures = new HashSet<>();

    @OneToMany(mappedBy = "objectDetailDescription", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Advantage> advantages = new HashSet<>();

    @OneToMany(mappedBy = "objectDetailDescription", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Disadvantage> disadvantages = new HashSet<>();

}
