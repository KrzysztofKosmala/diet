package com.kosmala.springbootapp.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeMultiplierMealNumber
{
    RecipePayload recipe;
    int mealNumber;
}
