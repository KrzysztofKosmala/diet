package com.kosmala.springbootapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class DetailedUserInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GenderName gender;

    private int amount_of_meals;

    private int caloric_intake;

    private int weight;

    private int age;

}