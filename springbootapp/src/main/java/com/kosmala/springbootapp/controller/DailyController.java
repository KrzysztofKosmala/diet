package com.kosmala.springbootapp.controller;

import com.kosmala.springbootapp.entity.*;
import com.kosmala.springbootapp.payload.*;
import com.kosmala.springbootapp.repository.DailyConsumptionProductAmountRepository;
import com.kosmala.springbootapp.repository.DailyConsumptionRepository;
import com.kosmala.springbootapp.repository.ProductRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @GetMapping
    public DailyResponse init(@AuthenticationPrincipal UserPrincipal currentUser, @RequestParam String date)
    {
        DailyResponse dailyResponse = new DailyResponse();

        if (!dailyConsumptionRepository.existsByUserIdAndDate(currentUser.getId(), date)) {
            DailyConsumption dailyConsumption = new DailyConsumption();


            userRepository.findByUsername(currentUser.getUsername()).ifPresent(e ->
                    {
                        final DetailedUserInfo detailedUserInfo = e.getDetailedUserInfo();
                        dailyResponse.setKcal(detailedUserInfo.getCaloric_intake());
                        dailyResponse.setAmountOfMeals(detailedUserInfo.getAmount_of_meals());
                        dailyResponse.setProtein(detailedUserInfo.getProtein());
                        dailyResponse.setFat(detailedUserInfo.getFat());
                        dailyResponse.setCarbo(detailedUserInfo.getCarbo());
                        dailyConsumption.setUser(e);
                        dailyConsumption.setCurrentAmountOfMeals(detailedUserInfo.getAmount_of_meals());

                    }
            );
            dailyConsumption.setDate(date);
            dailyConsumptionRepository.save(dailyConsumption);
            return dailyResponse;
        }


        DailyConsumption daily = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), date);

        DetailedUserInfo detailedUserInfo = daily.getUser().getDetailedUserInfo();

        dailyResponse.setAmountOfMeals(daily.getCurrentAmountOfMeals());
        dailyResponse.setCarbo(detailedUserInfo.getCarbo());
        dailyResponse.setProtein(detailedUserInfo.getProtein());
        dailyResponse.setFat(detailedUserInfo.getFat());
        dailyResponse.setKcal(detailedUserInfo.getCaloric_intake());
        dailyResponse.setDate(date);

        List<ProductAmountMealNumber> productAmountMealNumbers = new ArrayList<>();
        daily.getProducts().forEach(dailyConsumptionProductAmount ->
        {
            dailyConsumptionProductAmount.getMealAmounts().forEach(mealAmount -> {

                ProductAmountMealNumber productAmountMealNumber = new ProductAmountMealNumber();

                ProductRequest productRequest = new ProductRequest();

                Product productFromDailyConsumption = dailyConsumptionProductAmount.getProduct();
                productRequest.setAmount(mealAmount.getAmount());
                productRequest.setMin_value(productFromDailyConsumption.getMin_value());
                productRequest.setName(productFromDailyConsumption.getName());
                productRequest.setMetric(productFromDailyConsumption.getMetric().name());
                productRequest.setKcal(productFromDailyConsumption.getKcal());
                productRequest.setFat(productFromDailyConsumption.getFat());
                productRequest.setCarbo(productFromDailyConsumption.getCarbo());
                productRequest.setProtein(productFromDailyConsumption.getProtein());
                productRequest.setDivisible(productFromDailyConsumption.isDivisible());

                productAmountMealNumber.setProduct(productRequest);
                productAmountMealNumber.setMealNumber(mealAmount.getMealNumber());
                productAmountMealNumbers.add(productAmountMealNumber);
            });

        });
        dailyResponse.setProductAmountMealNumbers(productAmountMealNumbers);
        return dailyResponse;

    }


    @PostMapping("/update")
    public ResponseEntity updateDaily(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody List<ProductAmountMealNumber> dailyRequest, @RequestParam String date)
    {
        Set<DailyConsumptionProductAmount> products = new HashSet<>();
        DailyConsumption daily = dailyConsumptionRepository.findByUserIdAndDate(currentUser.getId(), date);

        DailyConsumption updatedDaily = new DailyConsumption();

        updatedDaily.setCurrentAmountOfMeals(daily.getCurrentAmountOfMeals());
        updatedDaily.setDate(date);
        updatedDaily.setUser(daily.getUser());
        dailyConsumptionRepository.delete(daily);
        dailyConsumptionRepository.save(updatedDaily);



        Map<String, List<ProductAmountMealNumber>> groupedDailyRequestByNameOfProduct = dailyRequest.stream().collect(Collectors.groupingBy(e -> e.getProduct().getName()));

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







/*

        if(!dailyRequest.isEmpty())
        {
            dailyRequest.forEach(productInfo ->
            {
                DailyConsumptionProductAmount dailyConsumptionProductAmount = new DailyConsumptionProductAmount();
                dailyConsumptionProductAmount.setDaily(updatedDaily);
                dailyConsumptionProductAmount.setProduct(productRepository.findByName(productInfo.getProduct().getName()));


                MealAmount mealAmount = new MealAmount();


                dailyConsumptionProductAmount.setMealNumber(productInfo.getMealNumber());
                dailyConsumptionProductAmount.setAmount(productInfo.getProduct().getAmount());

                products.add(dailyConsumptionProductAmount);
                dailyConsumptionProductAmountRepository.save(dailyConsumptionProductAmount);

            });
            updatedDaily.setProducts(products);
        }

*/

        //same for recipies

        return ResponseEntity.ok(new CustomResponse(true, "Daily has been updated successfully"));
    }
}
