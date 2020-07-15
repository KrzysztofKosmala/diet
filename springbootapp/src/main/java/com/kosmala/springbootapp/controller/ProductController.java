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
import java.util.List;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestBody ProductRequest productRequest) throws JsonProcessingException
    {
        if(productRepository.existsByName(productRequest.getName())) {
            return new ResponseEntity(new CustomResponse(false, "This product is already in DB!"),
                    HttpStatus.BAD_REQUEST);
        }
        Product product = new Product();
        //for 100g or 100ml or one piece
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

    @GetMapping()
    public ResponseEntity getProductByName(@RequestParam String name)
    {
        final Product byName = productRepository.findByName(name);
        if(byName != null)
        return ResponseEntity.ok(byName);
        else return new ResponseEntity(new CustomResponse(false, "There is no such product!"),
                HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/nameLike")
    public ResponseEntity getProductByNameLike(@RequestParam String nameLike)
    {
        if(nameLike.length()>0)
        {
            final List<Product> collect = productRepository.findByNameContains(nameLike).stream().limit(5).collect(Collectors.toList());
            if(collect.size()>0)
            return ResponseEntity.ok(collect);
            else return new ResponseEntity(new CustomResponse(false, "There is no such product!"),
                HttpStatus.BAD_REQUEST);

        }
        else return new ResponseEntity(new CustomResponse(false, "Please pass at least one digit"),
                HttpStatus.BAD_REQUEST);
    }



    public double countKcal(double protein, double fat, double carbo)
    {
        return DoubleRounder.round((4*protein)+(9*fat)+(4*carbo), 1);
    }

}
