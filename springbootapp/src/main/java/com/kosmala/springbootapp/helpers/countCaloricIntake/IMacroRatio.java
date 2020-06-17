package com.kosmala.springbootapp.helpers.countCaloricIntake;

import com.kosmala.springbootapp.entity.GenderName;

public interface IMacroRatio
{
        public Ratio countRatio(GenderName sex, int caloricIntake, int weight);
}
