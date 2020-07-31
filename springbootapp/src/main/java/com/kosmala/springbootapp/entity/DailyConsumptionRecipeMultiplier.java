package com.kosmala.springbootapp.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_consumption_recipe_multipier")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DailyConsumptionRecipeMultiplier implements Serializable
{
    @EmbeddedId
    private DailyConsumptionRecipeMultiplierId id = new DailyConsumptionRecipeMultiplierId();

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("dailyId")
    private DailyConsumption daily;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("recipeId")
    private Recipe recipe;

    @ElementCollection
    List<MealMultiplier> mealMultiplier = new ArrayList<>();
}
