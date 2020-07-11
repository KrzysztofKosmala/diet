package com.kosmala.springbootapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosmala.springbootapp.entity.Product;
import com.kosmala.springbootapp.entity.Recipe;
import com.kosmala.springbootapp.entity.RecipeProductAmount;
import com.kosmala.springbootapp.entity.TypeOfRecipe;
import com.kosmala.springbootapp.helpers.countCaloricIntake.PFCRatio;
import com.kosmala.springbootapp.payload.CustomResponse;
import com.kosmala.springbootapp.payload.ProductRequest;
import com.kosmala.springbootapp.payload.RecipeRequest;
import com.kosmala.springbootapp.repository.ProductRepository;
import com.kosmala.springbootapp.repository.RecipeProductAmountRepository;
import com.kosmala.springbootapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController
{

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RecipeProductAmountRepository recipeProductAmountRepository;


    @GetMapping("/getIngredients")
    public ResponseEntity getIngredients(@RequestParam String nameOfRecipe)
    {
        return ResponseEntity.ok(recipeRepository.findByName(nameOfRecipe).getProducts().stream().map(this::recipeProductAmountToProductRequest).collect(Collectors.toList()));
    }

    @PostMapping("/create")
    public ResponseEntity createRecipe(@RequestBody RecipeRequest recipeRequest)throws JsonProcessingException
    {
        Recipe recipe = new Recipe();
        recipe.setName(recipeRequest.getName());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setType(TypeOfRecipe.valueOf(recipeRequest.getType()));

        recipe.setKcal(recipeRequest.getProducts().stream().mapToDouble(product -> product.getKcal() * product.getAmount()).sum());

        PFCRatio pfcRatio = countPFCRatio(recipeRequest.getProducts());
        recipe.setProtein_ratio(pfcRatio.getProtein_ratio());
        recipe.setFat_ratio(pfcRatio.getFat_ratio());
        recipe.setCarbo_ratio(pfcRatio.getCarbo_ratio());

        Set<RecipeProductAmount> recipeProductAmounts = new HashSet<>();
        recipeRequest.getProducts().forEach(productRequest -> {
            RecipeProductAmount recipeProductAmount = new RecipeProductAmount();
            recipeProductAmount.setProduct(productRepository.findByName(productRequest.getName()));
            recipeProductAmount.setAmount(productRequest.getAmount());
            recipeProductAmount.setRecipe(recipe);
            recipeProductAmounts.add(recipeProductAmount);
        });

        recipe.setProducts(recipeProductAmounts);
        recipeRepository.save(recipe);

        return ResponseEntity.ok(new CustomResponse(true, "New recipe has been added successfully!"));
    }

    public PFCRatio countPFCRatio(List<ProductRequest> productRequestList)
    {
        PFCRatio pfcRatio = new PFCRatio();

        double protein = productRequestList.stream().mapToDouble(product -> product.getProtein() * product.getAmount()).sum();
        double fat = productRequestList.stream().mapToDouble(product -> product.getFat() * product.getAmount()).sum();
        double carbo = productRequestList.stream().mapToDouble(product -> product.getCarbo() * product.getAmount()).sum();;

        Optional<Double> standardizationValue = Stream.of(protein, fat, carbo).sorted().filter(value -> value > 0).findFirst();

        standardizationValue.ifPresent(
                aDouble -> {
                    //add round
                    pfcRatio.setProtein_ratio(protein / aDouble);
                    pfcRatio.setFat_ratio(fat / aDouble);
                    pfcRatio.setCarbo_ratio(carbo / aDouble);
                }
        );

        return pfcRatio;
    }

    ProductRequest recipeProductAmountToProductRequest(RecipeProductAmount  recipeProductAmount)
    {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setAmount(recipeProductAmount.getAmount());
        productRequest.setCarbo(recipeProductAmount.getProduct().getCarbo());
        productRequest.setDivisible(recipeProductAmount.getProduct().isDivisible());
        productRequest.setProtein(recipeProductAmount.getProduct().getProtein());
        productRequest.setFat(recipeProductAmount.getProduct().getFat());
        productRequest.setKcal(recipeProductAmount.getProduct().getKcal());
        productRequest.setMetric(recipeProductAmount.getProduct().getMetric().name());
        productRequest.setMin_value(recipeProductAmount.getProduct().getMin_value());
        productRequest.setName(recipeProductAmount.getProduct().getName());
        return productRequest;
    }



}
