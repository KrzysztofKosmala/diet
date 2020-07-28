package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DailyConsumptionRecipeMultiplier;
import com.kosmala.springbootapp.entity.DailyConsumptionRecipeMultiplierId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyConsumptionRecipeMultiplierRepository extends JpaRepository<DailyConsumptionRecipeMultiplier, DailyConsumptionRecipeMultiplierId>
{
}
