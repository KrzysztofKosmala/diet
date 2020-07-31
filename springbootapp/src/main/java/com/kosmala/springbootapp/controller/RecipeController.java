package com.kosmala.springbootapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosmala.springbootapp.entity.Metric;
import com.kosmala.springbootapp.entity.Recipe;
import com.kosmala.springbootapp.entity.RecipeProductAmount;
import com.kosmala.springbootapp.entity.TypeOfRecipe;
import com.kosmala.springbootapp.helpers.countCaloricIntake.PFCRatio;
import com.kosmala.springbootapp.payload.CustomResponse;
import com.kosmala.springbootapp.payload.PFCK;
import com.kosmala.springbootapp.payload.ProductPayload;
import com.kosmala.springbootapp.payload.RecipePayload;
import com.kosmala.springbootapp.repository.ProductRepository;
import com.kosmala.springbootapp.repository.RecipeProductAmountRepository;
import com.kosmala.springbootapp.repository.RecipeRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collector;
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
    public ResponseEntity createRecipe(@RequestBody RecipePayload recipePayload)throws JsonProcessingException
    {
        Recipe recipe = new Recipe();
        recipe.setName(recipePayload.getName());
        recipe.setDescription(recipePayload.getDescription());
        recipe.setType1(TypeOfRecipe.valueOf(recipePayload.getType1()));
        if(recipePayload.getType2() != null)
        recipe.setType2(TypeOfRecipe.valueOf(recipePayload.getType2()));
        double sumOfKcalFromProductsWithMetricNotEqualToPC = recipePayload.getProducts()
                .stream()
                .filter(product -> !product.getMetric().equals("PC"))
                .mapToDouble(product -> (product.getKcal() * product.getAmount()) /100)
                .sum();
        double sumOfKcalFromProductsWithMetricEqualToPC = recipePayload.getProducts()
                .stream()
                .filter(product -> product.getMetric().equals("PC"))
                .mapToDouble(product -> (product.getKcal() * product.getAmount()))
                .sum();

        recipe.setKcal(sumOfKcalFromProductsWithMetricNotEqualToPC + sumOfKcalFromProductsWithMetricEqualToPC);

        PFCRatio pfcRatio = countPFCRatio(recipePayload.getProducts());
        recipe.setProtein_ratio(pfcRatio.getProtein_ratio());
        recipe.setFat_ratio(pfcRatio.getFat_ratio());
        recipe.setCarbo_ratio(pfcRatio.getCarbo_ratio());

        Set<RecipeProductAmount> recipeProductAmounts = new HashSet<>();
        recipePayload.getProducts().forEach(productRequest -> {
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

    @GetMapping("/hintTheRecipe")
    public RecipePayload findRecipeBasedOnNeededPFCK(@AuthenticationPrincipal UserPrincipal currentUser, @RequestParam double protein, @RequestParam double fat, @RequestParam double carbo, @RequestParam double kcal)
    {
            Recipe recipe = recipeRepository.findByName("test");
            RecipePayload recipePayload = new RecipePayload(recipe, 2.0);

            return recipePayload;
    }

    public PFCRatio countPFCRatio(List<ProductPayload> productPayloadList)
    {
        PFCRatio pfcRatio = new PFCRatio();

        double protein = productPayloadList.stream()
                .filter(productPayload -> productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getProtein() * product.getAmount())
                .sum() + productPayloadList.stream()
                .filter(productPayload -> !productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getProtein() * product.getAmount() /100)
                .sum();
        double fat = productPayloadList.stream()
                .filter(productPayload -> productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getFat() * product.getAmount())
                .sum() + productPayloadList.stream()
                .filter(productPayload -> !productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getFat() * product.getAmount() /100)
                .sum();
        double carbo = productPayloadList.stream()
                .filter(productPayload -> productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getCarbo() * product.getAmount())
                .sum() + productPayloadList.stream()
                .filter(productPayload -> !productPayload.getMetric().equals(Metric.PC.name()))
                .mapToDouble(product -> product.getCarbo() * product.getAmount() /100)
                .sum();



        Optional<Double> standardizationValue = Stream.of(protein, fat, carbo).sorted().filter(value -> value > 0).findFirst();

        standardizationValue.ifPresent(
                aDouble -> {

                    pfcRatio.setProtein_ratio(DoubleRounder.round(protein / aDouble, 2));
                    pfcRatio.setFat_ratio(DoubleRounder.round(fat / aDouble, 2));
                    pfcRatio.setCarbo_ratio(DoubleRounder.round(carbo / aDouble,2));
                }
        );

        return pfcRatio;
    }

    ProductPayload recipeProductAmountToProductRequest(RecipeProductAmount  recipeProductAmount)
    {
        ProductPayload productPayload = new ProductPayload();
        productPayload.setAmount(recipeProductAmount.getAmount());
        productPayload.setCarbo(recipeProductAmount.getProduct().getCarbo());
        productPayload.setDivisible(recipeProductAmount.getProduct().isDivisible());
        productPayload.setProtein(recipeProductAmount.getProduct().getProtein());
        productPayload.setFat(recipeProductAmount.getProduct().getFat());
        productPayload.setKcal(recipeProductAmount.getProduct().getKcal());
        productPayload.setMetric(recipeProductAmount.getProduct().getMetric().name());
        productPayload.setMin_value(recipeProductAmount.getProduct().getMin_value());
        productPayload.setName(recipeProductAmount.getProduct().getName());
        return productPayload;
    }

}
