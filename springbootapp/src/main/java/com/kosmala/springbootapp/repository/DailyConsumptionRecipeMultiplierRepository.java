package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DailyConsumptionRecipeMultiplier;
import com.kosmala.springbootapp.entity.DailyConsumptionRecipeMultiplierId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyConsumptionRecipeMultiplierRepository extends JpaRepository<DailyConsumptionRecipeMultiplier, DailyConsumptionRecipeMultiplierId>
{
    List<DailyConsumptionRecipeMultiplier> findDailyConsumptionRecipeMultipliersByDailyId(Long dailyId);
}
