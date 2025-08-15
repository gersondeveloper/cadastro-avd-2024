package com.gersondeveloper.cadastroavd2024.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_variant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_attributes", columnNames = {"product_id", "attributes"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductVariant extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Store JSON attributes as text; database column uses JSONB type
    @Column(columnDefinition = "jsonb")
    private String attributes;
}
