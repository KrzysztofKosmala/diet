package com.kosmala.springbootapp.controller;

import com.kosmala.springbootapp.entity.*;
import com.kosmala.springbootapp.payload.*;
import com.kosmala.springbootapp.repository.*;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/daily")
public class DailyController
{

    @Autowired
    DailyConsumptionRepository dailyConsumptionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DailyConsumptionProductAmountRepository dailyConsumptionProductAmountRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    DailyConsumptionRecipeMultiplierRepository dailyConsumptionRecipeMultiplierRepository;

    @GetMapping
    public DailyPayload init(@AuthenticationPrincipal UserPrincipal currentUser, @RequestParam String date)
    {
        DailyPayload dailyPayload = new DailyPayload();

        if (!dailyConsumptionRepository.existsByUserIdAndDate(currentUser.getId(), date))
        {

            DailyConsumption dailyConsumption = new DailyConsumption();


            userRepository.findByUsername(currentUser.getUsername()).ifPresent(e ->
                    {
                        final DetailedUserInfo detailedUserInfo = e.getDetailedUserInfo();
                        dailyPayload.setKcal(detailedUserInfo.getCaloric_intake());
                        dailyPayload.setAmountOfMeals(detailedUserInfo.getAmount_of_meals());
                        dailyPayload.setProtein(detailedUserInfo.getProtein());
                        dailyPayload.setFat(detailedUserInfo.getFat());
                        dailyPayload.setCarbo(detailedUserInfo.getCarbo());
                        dailyConsumption.setUser(e);
                        dailyConsumption.setCurrentAmountOfMeals(detailedUserInfo.getAmount_of_meals());

                    }
            );
            dailyConsumption.setDate(date);
            dailyConsumptionRepository.save(dailyConsumption);
            return dailyPayload;
        }


        DailyConsumption daily = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), date);



        DetailedUserInfo detailedUserInfo = daily.getUser().getDetailedUserInfo();

        dailyPayload.setAmountOfMeals(daily.getCurrentAmountOfMeals());
        dailyPayload.setCarbo(detailedUserInfo.getCarbo());
        dailyPayload.setProtein(detailedUserInfo.getProtein());
        dailyPayload.setFat(detailedUserInfo.getFat());
        dailyPayload.setKcal(detailedUserInfo.getCaloric_intake());
        dailyPayload.setDate(date);

        List<ProductAmountMealNumber> productAmountMealNumbers = new ArrayList<>();
        daily.getProducts().forEach(dailyConsumptionProductAmount ->
        {
            dailyConsumptionProductAmount.getMealAmounts().forEach(mealAmount ->
            {

                ProductAmountMealNumber productAmountMealNumber = new ProductAmountMealNumber();

                ProductPayload productPayload = new ProductPayload();

                Product productFromDailyConsumption = dailyConsumptionProductAmount.getProduct();
                productPayload.setAmount(mealAmount.getAmount());
                productPayload.setMin_value(productFromDailyConsumption.getMin_value());
                productPayload.setName(productFromDailyConsumption.getName());
                productPayload.setMetric(productFromDailyConsumption.getMetric().name());
                productPayload.setKcal(productFromDailyConsumption.getKcal());
                productPayload.setFat(productFromDailyConsumption.getFat());
                productPayload.setCarbo(productFromDailyConsumption.getCarbo());
                productPayload.setProtein(productFromDailyConsumption.getProtein());
                productPayload.setDivisible(productFromDailyConsumption.isDivisible());

                productAmountMealNumber.setProduct(productPayload);
                productAmountMealNumber.setMealNumber(mealAmount.getMealNumber());
                productAmountMealNumbers.add(productAmountMealNumber);
            });

        });
        dailyPayload.setProductAmountMealNumbers(productAmountMealNumbers);



        List<RecipeMultiplierMealNumber> recipesMultiplierMealNumber = new ArrayList<>();
        daily.getRecipes().forEach(recipe ->
        {
            RecipeMultiplierMealNumber recipeMultiplierMealNumber = new RecipeMultiplierMealNumber();
            RecipePayload recipePayload = new RecipePayload(recipe.getRecipe());
            recipePayload.setMultiplier(recipe.getMultiplier());
            recipeMultiplierMealNumber.setRecipe(recipePayload);
            recipeMultiplierMealNumber.setMealNumber(recipe.getMealNumber());
            recipesMultiplierMealNumber.add(recipeMultiplierMealNumber);
        });

        dailyPayload.setRecipesMultiplierMealNumber(recipesMultiplierMealNumber);

        return dailyPayload;

    }


    @PostMapping("/update")
    public ResponseEntity updateDaily(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody DailyPayload dailyPayload, @RequestParam String date)
    {
        Set<DailyConsumptionProductAmount> products = new HashSet<>();
        Set<DailyConsumptionRecipeMultiplier> recipes = new HashSet<>();
        DailyConsumption daily = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), date);

        DailyConsumption updatedDaily = new DailyConsumption();

        updatedDaily.setCurrentAmountOfMeals(daily.getCurrentAmountOfMeals());
        updatedDaily.setDate(date);
        updatedDaily.setUser(daily.getUser());
        dailyConsumptionRepository.delete(daily);
        dailyConsumptionRepository.save(updatedDaily);



        Map<String, List<ProductAmountMealNumber>> groupedDailyRequestByNameOfProduct =
                dailyPayload.getProductAmountMealNumbers().stream().collect(Collectors.groupingBy(e -> e.getProduct().getName()));

        for(Map.Entry<String, List<ProductAmountMealNumber>> pair : groupedDailyRequestByNameOfProduct.entrySet())
        {
            DailyConsumptionProductAmount dailyConsumptionProductAmount = new DailyConsumptionProductAmount();
            dailyConsumptionProductAmount.setDaily(updatedDaily);
            dailyConsumptionProductAmount.setProduct(productRepository.findByName(pair.getKey()));
            pair.getValue().forEach(productAmountMealNumber ->
            {
                MealAmount mealAmount = new MealAmount();
                mealAmount.setAmount(productAmountMealNumber.getProduct().getAmount());
                mealAmount.setMealNumber(productAmountMealNumber.getMealNumber());
                dailyConsumptionProductAmount.getMealAmounts().add(mealAmount);
            });
            products.add(dailyConsumptionProductAmount);
            dailyConsumptionProductAmountRepository.save(dailyConsumptionProductAmount);
        }
        updatedDaily.setProducts(products);


        dailyPayload.getRecipesMultiplierMealNumber().forEach(recipe ->
        {
            DailyConsumptionRecipeMultiplier dailyConsumptionRecipeMultiplier = new DailyConsumptionRecipeMultiplier();
            dailyConsumptionRecipeMultiplier.setDaily(updatedDaily);
            dailyConsumptionRecipeMultiplier.setRecipe(recipeRepository.findByName(recipe.getRecipe().getName()));
            dailyConsumptionRecipeMultiplier.setMealNumber(recipe.getMealNumber());
            dailyConsumptionRecipeMultiplier.setMultiplier(recipe.getRecipe().getMultiplier());
            recipes.add(dailyConsumptionRecipeMultiplier);
            dailyConsumptionRecipeMultiplierRepository.save(dailyConsumptionRecipeMultiplier);
        });

        updatedDaily.setRecipes(recipes);


        return ResponseEntity.ok(new CustomResponse(true, "Daily has been updated successfully"));
    }
}
