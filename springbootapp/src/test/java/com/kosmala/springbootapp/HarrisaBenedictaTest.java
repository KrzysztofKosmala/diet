package com.kosmala.springbootapp;

import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.GenderName;
import com.kosmala.springbootapp.entity.GoalName;
import com.kosmala.springbootapp.helpers.countCaloricIntake.HarrisaBenedicta;
import com.kosmala.springbootapp.helpers.countCaloricIntake.ICaloricIntake;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class HarrisaBenedictaTest
{

    HarrisaBenedicta harrisaBenedicta = new HarrisaBenedicta();


    @Test
    void count_bmr()
    {

        assertEquals(1952.9, harrisaBenedicta.countBmr(GenderName.MALE, 80, 22, 188));
    }

/*    @Enumerated(EnumType.STRING)
    private GenderName gender;
    private int amount_of_meals;
    private int caloric_intake;
    private int weight;
    private int age;
    private int height ;
    private double activity;
    private int protein;
    private int fat;
    private int carbo;
    @Enumerated(EnumType.STRING)
    private GoalName goal;*/
    @Test
    void count()
    {
        DetailedUserInfo detailedUserInfo = new DetailedUserInfo();
        detailedUserInfo.setGender(GenderName.MALE);
        detailedUserInfo.setWeight(80);
        detailedUserInfo.setAge(22);
        detailedUserInfo.setHeight(188);
        detailedUserInfo.setActivity(1.4);
        detailedUserInfo.setGoal(GoalName.BULK);

        assertEquals(3144, harrisaBenedicta.count(detailedUserInfo));
    }
}
