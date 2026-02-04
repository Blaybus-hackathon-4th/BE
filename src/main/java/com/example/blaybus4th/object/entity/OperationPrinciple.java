package com.example.blaybus4th.object.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OperationPrinciple")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class OperationPrinciple {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationPrincipleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_detail_id", nullable = false)
    private ObjectDetailDescription objectDetailDescription;

    @Column(nullable = false,name = "operation_principle_description")
    private String operationPrincipleDescription;

    @Column(nullable = false,name = "operation_principle_order")
    private Integer operationPrincipleOrder;

}
