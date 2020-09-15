package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Recipe;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.kosmala.springbootapp.entity.TypeOfRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, Long>
{
    Recipe findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM recipe WHERE ABS(protein_ratio - :p) < (:p * 2)/10 AND ABS(fat_ratio - :f) < (:f * 2)/10 AND ABS(carbo_ratio - :c) < (:c * 3)/10 ")
   List<Recipe> findBestMatches(@Param("p") double p, @Param("f") double f, @Param("c") double c);
}

