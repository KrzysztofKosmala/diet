package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Recipe;
import com.kosmala.springbootapp.entity.RecipeProductAmount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.decimal4j.util.DoubleRounder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class  RecipePayload
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

    public RecipePayload(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public RecipePayload(Recipe recipe, double multiplier)
    {
        this.name = recipe.getName();
        this.description = recipe.getDescription();

        this.type1 = recipe.getType1().name();

        if(recipe.getType2() != null)
            this.type2 = recipe.getType2().name();

        this.multiplier = findFinalMultiplier(recipe.getProducts(), multiplier);
        this.products = recipe.getProducts().stream()
                .map(productAmount ->
                {
                    ProductPayload productPayload = new ProductPayload(productAmount.getProduct());
                    productPayload.setAmount(DoubleRounder.round(productAmount.getAmount() * multiplier, 1));
                    this.multiplier = multiplier;
                    return productPayload;
                }).collect(Collectors.toList());
        this.kcal = recipe.getKcal() * multiplier;
    }

    private double findFinalMultiplier(Set<RecipeProductAmount> products, double multiplier)
    {
        AtomicReference<Double> currentMultiplier = new AtomicReference<Double>(multiplier);
        List<RecipeProductAmount> indivisible =  products.stream()
                .filter(recipeProductAmount -> !recipeProductAmount.getProduct().isDivisible()).collect(Collectors.toList());

        if(indivisible.size() > 0)
        {
            indivisible.forEach(recipeProductAmount -> {
                if((recipeProductAmount.getAmount()* currentMultiplier.get()) % 1 != 0)
                {
                    currentMultiplier.set(DoubleRounder.round((recipeProductAmount.getAmount() * currentMultiplier.get() / recipeProductAmount.getAmount()), 0));
                }
            });
        }

        List<RecipeProductAmount> collect = products.stream()
                .filter((recipeProductAmount -> recipeProductAmount.getAmount() * currentMultiplier.get() < recipeProductAmount.getProduct().getMin_value()))
                .collect(Collectors.toList());

        if(collect.size() == 0)
            return currentMultiplier.get();
        else
        return collect.stream().mapToDouble(value -> value.getProduct().getMin_value() / value.getAmount()).max().getAsDouble();

    }
}
