package com.kosmala.springbootapp.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class DetailedUserInfoRequest
{
    private String gender;

    private int amount_of_meals;

    private int caloric_intake;

    private int weight;

    private int age;

    private int height;

    private int protein;

    private int fat;

    private int carbo;

    private double activity;

    private String goal;
}
