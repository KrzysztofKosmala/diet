package com.kosmala.springbootapp.helpers.countCaloricIntake;

import com.kosmala.springbootapp.entity.GenderName;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Component;

@Component
public class SzmexyRatio implements IMacroRatio
{
    @Override
    public Ratio countRatio(GenderName sex, int caloricIntake, int weight)
    {
        int protein = 2*weight;
        int fat = (int) DoubleRounder.round((caloricIntake * 0.25) / 9, 0);
        int carbo = (caloricIntake - (protein*4 + fat*9)) / 4;

        Ratio ratio = new Ratio();

        ratio.setProtein(protein);
        ratio.setFat(fat);
        ratio.setCarbo(carbo);

        return ratio;
    }
}
