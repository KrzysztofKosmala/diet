package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Recipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class RecipePayload
{
    private String name;
    private String description;
    private String type1;
    private String type2;
    private double multiplier;
    private double kcal;
    private List<ProductPayload> products = new ArrayList<>();

    public RecipePayload(Recipe recipe)
    {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.kcal = recipe.getKcal();
        this.type1 = recipe.getType1().name();
        if(recipe.getType2() != null)
        this.type2 = recipe.getType2().name();

        this.products = recipe.getProducts().stream().map(productAmount ->{
            ProductPayload productPayload = new ProductPayload(productAmount.getProduct());
            productPayload.setAmount(productAmount.getAmount());
            return productPayload;
        }).collect(Collectors.toList());
    }

    public RecipePayload(Recipe recipe, double multiplier)
    {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.kcal = recipe.getKcal();
        this.type1 = recipe.getType1().name();
        this.multiplier = multiplier;
        if(recipe.getType2() != null)
            this.type2 = recipe.getType2().name();

        this.products = recipe.getProducts().stream().map(productAmount ->{
            ProductPayload productPayload = new ProductPayload(productAmount.getProduct());
            productPayload.setAmount(productAmount.getAmount() * multiplier);
            return productPayload;
        }).collect(Collectors.toList());
    }
}
