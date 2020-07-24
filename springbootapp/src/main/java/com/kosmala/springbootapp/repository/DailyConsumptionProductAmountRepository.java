package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DailyConsumptionProductAmount;
import com.kosmala.springbootapp.entity.DailyConsumptionProductAmountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DailyConsumptionProductAmountRepository extends JpaRepository<DailyConsumptionProductAmount, DailyConsumptionProductAmountId>
{
}
