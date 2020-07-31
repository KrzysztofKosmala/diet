package com.kosmala.springbootapp.entity;

import lombok.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "shopping_list_product_amount")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ShoppingListProductAmount
{
    //ta kalasa ma łączyć w tabeli shopping_list_product_amount shoppinglist z produktem oraz iloscia
    @EmbeddedId
    private ShoppingListProductAmountId id = new ShoppingListProductAmountId();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("shoppingListId")
    private ShoppingList shoppingList;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("productId")
    private Product product;

    private double amount;

}
