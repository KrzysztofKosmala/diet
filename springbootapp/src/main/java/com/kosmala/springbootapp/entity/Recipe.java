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
    @Enumerated(EnumType.STRING)
    private TypeOfRecipe type;
    private double protein_ratio;
    private double fat_ratio;
    private double carbo_ratio;
    private double kcal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<RecipeProductAmount> products = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "recipe")
    private Set<DailyConsumptionRecipeMultiplier> daily = new HashSet<>();

}
