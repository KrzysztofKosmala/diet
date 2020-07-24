package com.kosmala.springbootapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class MealAmount
{
    int mealNumber;
    double amount;
}
