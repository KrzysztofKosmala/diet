package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long>
{
}
