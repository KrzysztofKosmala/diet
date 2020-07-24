package com.kosmala.springbootapp.payload;
import java.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyResponse
{
    int amountOfMeals;
    int kcal;
    String date;
    int protein;
    int fat;
    int carbo;
    ////////////////////
    List<ProductAmountMealNumber> productAmountMealNumbers;
}
