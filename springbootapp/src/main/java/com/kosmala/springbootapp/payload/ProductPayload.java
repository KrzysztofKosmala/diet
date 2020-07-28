package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Metric;
import com.kosmala.springbootapp.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductPayload
{
    private String name;
    private double protein;
    private double fat;
    private double carbo;
    private double min_value;
    private String metric;
    private double kcal;
    private boolean divisible;
    //needed only for recipe
    private double amount;

    ProductPayload(Product product)
    {
        this.name = product.getName();
        this.protein = product.getProtein();
        this.fat = product.getFat();
        this.carbo = product.getCarbo();
        this.min_value = product.getMin_value();
        this.metric = product.getMetric().name();
        this.kcal = product.getKcal();
        this.divisible = product.isDivisible();

    }

}
