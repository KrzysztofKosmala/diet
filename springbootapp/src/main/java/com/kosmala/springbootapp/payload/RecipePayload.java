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
    private String type;
    private double multiplier;
    private double kcal;
    private List<ProductPayload> products = new ArrayList<>();

    public RecipePayload(Recipe recipe)
    {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.kcal = recipe.getKcal();
        this.type = recipe.getType().name();

        this.products = recipe.getProducts().stream().map(productAmount ->{
            ProductPayload productPayload = new ProductPayload(productAmount.getProduct());
            productPayload.setAmount(productAmount.getAmount());
            return productPayload;
        }).collect(Collectors.toList());
    }
}
