package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DailyConsumption;
import com.kosmala.springbootapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyConsumptionRepository extends JpaRepository<DailyConsumption, Long>
{
    public boolean existsByUserIdAndDate(Long userId, String date);
    public DailyConsumption findByUserIdAndDate(Long userId, String date);


    List<DailyConsumption> findByUser(Optional<User> user);
}
