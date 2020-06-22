package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DailyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyConsumptionRepository extends JpaRepository<DailyConsumption, Long>
{
}
