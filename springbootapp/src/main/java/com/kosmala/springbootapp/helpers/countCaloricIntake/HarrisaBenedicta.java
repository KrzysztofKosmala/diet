package com.kosmala.springbootapp.helpers.countCaloricIntake;

import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.GenderName;
import com.kosmala.springbootapp.entity.GoalName;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("HarrisaBenedicta")
public class HarrisaBenedicta implements ICaloricIntake
{



    @Override
    public int count(DetailedUserInfo detailedUserInfo)
    {
        double DED =  countBmr(detailedUserInfo.getGender(), detailedUserInfo.getWeight(), detailedUserInfo.getAge(), detailedUserInfo.getHeight()) * detailedUserInfo.getActivity();

        if(detailedUserInfo.getGoal().equals(GoalName.BULK))
            return (int) DoubleRounder.round(DED * 1.15, 0);
        else
            return (int) DoubleRounder.round(DED * 0.75, 0);
    }


    public double countBmr(GenderName sex, int weight, int age, int height)
    {
        if(sex.equals(GenderName.MALE))
        {


            return 66.5 + (13.7 * weight) + (5 * height) - (6.8 * age);
        }else
        {
            return 665 + (9.6 * weight) + (1.85 * height) - (4.7 * age);
        }
    }
}
