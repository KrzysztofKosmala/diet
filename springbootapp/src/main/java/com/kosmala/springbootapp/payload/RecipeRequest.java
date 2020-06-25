package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Product;
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
    private int protein_ratio;
    private int fat_ratio;
    private int carbo_ratio;
    private int kcal;
    private List<ProductRequest> productsName = new ArrayList<>();
}
