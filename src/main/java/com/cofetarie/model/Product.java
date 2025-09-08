package com.cofetarie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"products", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}

