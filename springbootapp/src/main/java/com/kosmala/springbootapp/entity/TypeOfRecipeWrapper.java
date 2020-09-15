package com.kosmala.springbootapp.entity;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;

public class TypeOfRecipeWrapper
{
    private Map<Integer, List<TypeOfRecipe>> helper = new HashMap<>();
    public TypeOfRecipeWrapper(int amountOfMeals)
    {
        switch (amountOfMeals){
            case 3:
            {
                helper.put(1, Collections.singletonList(TypeOfRecipe.BREAKFAST));
                helper.put(2, Arrays.asList(TypeOfRecipe.DINNER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                helper.put(3, Arrays.asList(TypeOfRecipe.SUPPER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                break;
            }
            case 4:
            {
                helper.put(1, Collections.singletonList(TypeOfRecipe.BREAKFAST));
                helper.put(2, Collections.singletonList(TypeOfRecipe.BREAKFAST));
                helper.put(3, Arrays.asList(TypeOfRecipe.DINNER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                helper.put(4, Arrays.asList(TypeOfRecipe.SUPPER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                break;
            }
            case 5:
            {
                helper.put(1, Collections.singletonList(TypeOfRecipe.BREAKFAST));
                helper.put(2, Collections.singletonList(TypeOfRecipe.BREAKFAST));
                helper.put(3, Arrays.asList(TypeOfRecipe.DINNER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                helper.put(4, Arrays.asList(TypeOfRecipe.DINNER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                helper.put(5, Arrays.asList(TypeOfRecipe.SUPPER, TypeOfRecipe.SNACK, TypeOfRecipe.DESSERT));
                break;
            }
        }

    }

    public List<TypeOfRecipe> getTypeOfMealsForMealNr(int mealNr)
    {
        return helper.get(mealNr);
    }
}
