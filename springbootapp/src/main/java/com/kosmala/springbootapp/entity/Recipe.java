package com.kosmala.springbootapp.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "recipe")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Recipe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int protein_ratio;
    private int fat_ratio;
    private int carbo_ratio;
    private int kcal;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_product",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

}
