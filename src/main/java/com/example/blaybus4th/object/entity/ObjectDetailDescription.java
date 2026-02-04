package com.example.blaybus4th.object.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ObjectDetailDescription")
@Getter
public class ObjectDetailDescription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "object_detail_id")
    private Long objectDetailId;

    @JoinColumn(name = "object_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Object object;

    @Column(nullable = false,name = "object_detail_description")
    private String objectDetailDescription;


}
