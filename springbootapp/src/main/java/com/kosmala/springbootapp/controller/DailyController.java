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
    public DailyPayload init(@AuthenticationPrincipal UserPrincipal currentUser, @RequestParam String currentDate, @RequestParam String dayBefore)
    {
        DailyPayload dailyPayload = new DailyPayload();

        if (!dailyConsumptionRepository.existsByUserIdAndDate(currentUser.getId(), currentDate))
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
                        dailyPayload.setExcludedRecipesFromDayBefore(new LinkedList<>());
                        dailyConsumption.setUser(e);
                        dailyConsumption.setCurrentAmountOfMeals(detailedUserInfo.getAmount_of_meals());

                    }
            );
            dailyConsumption.setDate(currentDate);
            dailyConsumptionRepository.save(dailyConsumption);
            return dailyPayload;
        }


        DailyConsumption daily = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), currentDate);



        DetailedUserInfo detailedUserInfo = daily.getUser().getDetailedUserInfo();

        dailyPayload.setAmountOfMeals(daily.getCurrentAmountOfMeals());
        dailyPayload.setCarbo(detailedUserInfo.getCarbo());
        dailyPayload.setProtein(detailedUserInfo.getProtein());
        dailyPayload.setFat(detailedUserInfo.getFat());
        dailyPayload.setKcal(detailedUserInfo.getCaloric_intake());
        dailyPayload.setDate(currentDate);


        DailyConsumption byUserIdAndDate = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), dayBefore);
        if(byUserIdAndDate != null)
        {
            List<DailyConsumptionRecipeMultiplier> dailyConsumptionRecipeMultipliersByDailyId = dailyConsumptionRecipeMultiplierRepository.findDailyConsumptionRecipeMultipliersByDailyId(byUserIdAndDate.getId());
            dailyPayload.setExcludedRecipesFromDayBefore(dailyConsumptionRecipeMultipliersByDailyId.stream()
                    .map(dailyConsumptionRecipeMultiplier -> dailyConsumptionRecipeMultiplier.getRecipe().getName())
                    .collect(Collectors.toList()));
        }

        List<ProductAmountMealNumber> productAmountMealNumbers = new ArrayList<>();
        daily.getProducts().forEach(dailyConsumptionProductAmount ->
        {
            dailyConsumptionProductAmount.getMealAmounts().forEach(mealAmount ->
            {

                ProductAmountMealNumber productAmountMealNumber = new ProductAmountMealNumber();

                ProductPayload productPayload = new ProductPayload();

                //na kostruktor zamienic
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
        daily.getRecipes().forEach(dailyConsumptionRecipeMultiplier ->
                {
                    dailyConsumptionRecipeMultiplier.getMealMultiplier().forEach(mealMultiplier ->
                    {
                        //na kostruktor zamienic
                        RecipeMultiplierMealNumber recipeMultiplierMealNumber = new RecipeMultiplierMealNumber();
                        Recipe recipe = dailyConsumptionRecipeMultiplier.getRecipe();
                        RecipePayload recipePayload = new RecipePayload(recipe, mealMultiplier.getMultiplier());
                        recipeMultiplierMealNumber.setRecipe(recipePayload);
                        recipeMultiplierMealNumber.setMealNumber(mealMultiplier.getMealNumber());
                        recipesMultiplierMealNumber.add(recipeMultiplierMealNumber);
                    });
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
            //na kostruktor zamienic
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

        Map<String, List<RecipeMultiplierMealNumber>> groupedDailyRequestByNameOfRecipe =
                dailyPayload.getRecipesMultiplierMealNumber().stream().collect(Collectors.groupingBy(e -> e.getRecipe().getName()));

        for(Map.Entry<String, List<RecipeMultiplierMealNumber>> pair : groupedDailyRequestByNameOfRecipe.entrySet())
        {
            //na kostruktor zamienic
            DailyConsumptionRecipeMultiplier dailyConsumptionRecipeMultiplier = new DailyConsumptionRecipeMultiplier();
            dailyConsumptionRecipeMultiplier.setDaily(updatedDaily);
            dailyConsumptionRecipeMultiplier.setRecipe(recipeRepository.findByName(pair.getKey()));
            pair.getValue().forEach(recipeMultiplierMealNumber ->
                    {
                        MealMultiplier mealMultiplier = new MealMultiplier();
                        mealMultiplier.setMealNumber(recipeMultiplierMealNumber.getMealNumber());
                        mealMultiplier.setMultiplier(recipeMultiplierMealNumber.getRecipe().getMultiplier());
                        dailyConsumptionRecipeMultiplier.getMealMultiplier().add(mealMultiplier);
                    });
            recipes.add(dailyConsumptionRecipeMultiplier);
            dailyConsumptionRecipeMultiplierRepository.save(dailyConsumptionRecipeMultiplier);
        }

        updatedDaily.setRecipes(recipes);


        return ResponseEntity.ok(new CustomResponse(true, "Daily has been updated successfully"));
    }
}
