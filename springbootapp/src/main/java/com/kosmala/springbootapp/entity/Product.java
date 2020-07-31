package com.kosmala.springbootapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double protein;
    private double fat;
    private double carbo;
    private double min_value;
    @Enumerated(EnumType.STRING)
    private Metric metric;
    private double kcal;
    private boolean divisible;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<RecipeProductAmount> recipe = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<DailyConsumptionProductAmount> daily = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<ShoppingListProductAmount> shoppingList = new HashSet<>();


}
