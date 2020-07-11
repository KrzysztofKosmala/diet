package com.kosmala.springbootapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reciepe_product_amount")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class RecipeProductAmount implements Serializable
{
    @EmbeddedId
    private RecipeProductAmountId id = new RecipeProductAmountId();


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    private Recipe recipe;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(name = "amount")
    private double amount;
}
