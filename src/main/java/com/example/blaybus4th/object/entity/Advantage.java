package com.example.blaybus4th.object.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "Advantage")
@NoArgsConstructor(access = PROTECTED)
public class Advantage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advantageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_detail_id", nullable = false)
    private ObjectDetailDescription objectDetailDescription;

    @Column(nullable = false, name = "advantage_description")
    private String advantageDescription;


}
