package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Recipe;
import com.kosmala.springbootapp.entity.RecipeProductAmount;
import com.kosmala.springbootapp.entity.RecipeProductAmountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeProductAmountRepository extends JpaRepository<RecipeProductAmount, RecipeProductAmountId>
{
}
