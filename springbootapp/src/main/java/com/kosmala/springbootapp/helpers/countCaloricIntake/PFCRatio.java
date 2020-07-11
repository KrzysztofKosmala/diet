package com.kosmala.springbootapp.helpers.countCaloricIntake;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PFCRatio
{
    private double protein_ratio;
    private double fat_ratio;
    private double carbo_ratio;
}
