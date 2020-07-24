package com.kosmala.springbootapp.payload;

import com.kosmala.springbootapp.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class ProductAmountMealNumber
{
    ProductRequest product;

    int mealNumber;
}
