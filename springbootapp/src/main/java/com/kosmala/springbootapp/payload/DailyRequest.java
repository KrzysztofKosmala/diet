package com.kosmala.springbootapp.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class DailyRequest
{
    ProductAmountMealNumber[] productAmountMealNumbers;
}
