package com.kosmala.springbootapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "daily_consumption")
@Getter
@Setter
@RequiredArgsConstructor
public class DailyConsumption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int currentAmountOfMeals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "daily", orphanRemoval = true, fetch=FetchType.EAGER)
    private Set<DailyConsumptionRecipeMultiplier> recipes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "daily", orphanRemoval = true, fetch=FetchType.EAGER)
    private Set<DailyConsumptionProductAmount> products = new HashSet<>();

}
