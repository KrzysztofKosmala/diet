package com.kosmala.springbootapp.helpers.countCaloricIntake;

import com.kosmala.springbootapp.entity.GenderName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class SzmexyRatioTest
{



    @Test
    void countRatio()
    {
        IMacroRatio macroRatio = new SzmexyRatio();
        Ratio ratio = macroRatio.countRatio(GenderName.MALE, 2500, 80);
        int protein = ratio.getProtein();
        int fat = ratio.getFat();
        int carbo = ratio.getCarbo();
        assertEquals(protein, 160);
        assertEquals(fat, 69);
        assertEquals(carbo, 309);


    }
}
