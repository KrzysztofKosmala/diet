package com.kosmala.springbootapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.Metric;
import com.kosmala.springbootapp.entity.Product;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.helpers.countCaloricIntake.Ratio;
import com.kosmala.springbootapp.payload.CustomResponse;
import com.kosmala.springbootapp.payload.DetailedUserInfoRequest;
import com.kosmala.springbootapp.payload.ProductRequest;
import com.kosmala.springbootapp.repository.ProductRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestBody ProductRequest productRequest) throws JsonProcessingException
    {


       //sprawdzanie jakies

        Product product = new Product();
        //
        product.setCarbo(productRequest.getCarbo());
        product.setFat(productRequest.getFat());
        product.setProtein(productRequest.getProtein());
        product.setDivisible(productRequest.isDivisible());
        product.setKcal(countKcal(productRequest.getProtein(), productRequest.getFat(), productRequest.getCarbo()));
        product.setMetric(Metric.valueOf(productRequest.getMetric()));
        product.setMin_value(productRequest.getMin_value());
        product.setName(productRequest.getName());

        productRepository.save(product);

        return ResponseEntity.ok(new CustomResponse(true, "Added product successfully"));
    }

    public double countKcal(double protein, double fat, double carbo)
    {
        return (4*protein)+(9*fat)+(4*carbo);
    }

}
