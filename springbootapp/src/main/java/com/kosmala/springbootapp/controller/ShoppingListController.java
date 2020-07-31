package com.kosmala.springbootapp.controller;

import com.kosmala.springbootapp.entity.Product;
import com.kosmala.springbootapp.entity.ShoppingList;
import com.kosmala.springbootapp.entity.ShoppingListProductAmount;
import com.kosmala.springbootapp.payload.*;
import com.kosmala.springbootapp.repository.ProductRepository;
import com.kosmala.springbootapp.repository.ShoppingListProductAmountRepository;
import com.kosmala.springbootapp.repository.ShoppingListRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/shoppingList")
public class ShoppingListController
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    ShoppingListRepository shoppingListRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShoppingListProductAmountRepository shoppingListProductAmountRepository;


    @GetMapping("/init")
    public ResponseEntity init(@AuthenticationPrincipal UserPrincipal currentUser)
    {
        userRepository.findByUsername(currentUser.getUsername()).ifPresent(loggedUser ->
        {
            if(!shoppingListRepository.existsByUserId(loggedUser.getId()))
            {
                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setUser(loggedUser);
                shoppingListRepository.save(shoppingList);
            }
        });
        return ResponseEntity.ok(new CustomResponse(true, "OK"));
    }

    @PostMapping("/addProducts")
    public ResponseEntity addProducts(@AuthenticationPrincipal UserPrincipal currentUser,@RequestBody ProductsWrapper products)
    {
        //zmienic zeby updatowal a nie usuwal

        //ProductsWrapper - trzyma liste produktów reprezentowanych jako dane do przesyłu. Ogólnie payload znaczy tyle że encje Product zamienia na jsona -> ProductPayload
        //products - to są produkty ktore maja zostac dodane do shopping list current usera


        ShoppingList shoppingList = shoppingListRepository.findByUserId(currentUser.getId());


        List<ProductPayload> productsOnList = shoppingList.getProducts()
                .stream()
                .map(shoppingListProductAmount -> new ProductPayload(shoppingListProductAmount.getProduct(),shoppingListProductAmount.getAmount()))
                .collect(Collectors.toList());
        ShoppingList updatedShoppingList = new ShoppingList();

        List<ProductPayload> productsOnListAndProductsToAdd = Stream.concat(productsOnList.stream(), products.getProducts().stream())
                .collect(Collectors.toList());


        userRepository.findByUsername(currentUser.getUsername()).ifPresent(loggedUser ->
        {
            updatedShoppingList.setUser(loggedUser);


            shoppingListRepository.delete(shoppingList);

            Map<String, List<ProductPayload>> grouped =
                    productsOnListAndProductsToAdd.stream()
                            .collect(Collectors.groupingBy(ProductPayload::getName));


            Set<ShoppingListProductAmount> newProductsForShoppingList = new HashSet<>();

            for (Map.Entry<String, List<ProductPayload>> next : grouped.entrySet()) {

                double countedAmountForAllProductsWithSameName = next.getValue()
                        .stream()
                        .mapToDouble(ProductPayload::getAmount)
                        .sum();


                ShoppingListProductAmount shoppingListProductAmount = new ShoppingListProductAmount();
                shoppingListProductAmount.setProduct(productRepository.findByName(next.getKey()));
                shoppingListProductAmount.setAmount(countedAmountForAllProductsWithSameName);
                shoppingListProductAmount.setShoppingList(updatedShoppingList);
                newProductsForShoppingList.add(shoppingListProductAmount);
            }
            updatedShoppingList.setProducts(newProductsForShoppingList);
            shoppingListRepository.save(updatedShoppingList);
        });

        return ResponseEntity.ok(new CustomResponse(true, "ok"));
    }

    @GetMapping
    public ProductsWrapper getShoppingList(@AuthenticationPrincipal UserPrincipal currentUser)
    {
        ShoppingList shoppingList = shoppingListRepository.findByUserId(currentUser.getId());
        List<ProductPayload> productPayloads = new ArrayList<>();
        shoppingList.getProducts().forEach(shoppingListProductAmount ->
        {
            productPayloads.add(new ProductPayload(shoppingListProductAmount.getProduct(), shoppingListProductAmount.getAmount()));
        });

        return new ProductsWrapper(productPayloads);
    }
}
