package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Product;
import com.kosmala.springbootapp.entity.TypeOfRecipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class RecipeRequest
{
    private String name;
    private String description;
    private String type;

    private double kcal;
    private List<ProductRequest> products = new ArrayList<>();
}
