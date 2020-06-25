package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Metric;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductRequest
{
    private String name;
    private double protein;
    private double fat;
    private double carbo;
    private double min_value;
    private String metric;
//    private int kcal;
    private boolean divisible;
    //needed only for recipe
    private double amount;

}
