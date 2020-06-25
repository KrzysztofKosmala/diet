package com.kosmala.springbootapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosmala.springbootapp.entity.Recipe;
import com.kosmala.springbootapp.payload.RecipeRequest;
import com.kosmala.springbootapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/recipe")
public class RecipeController
{

    @Autowired
    RecipeRepository recipeRepository;

    @PostMapping("/create")
    public ResponseEntity createRecipe(@RequestBody RecipeRequest recipeRequest)throws JsonProcessingException
    {
        Recipe recipe = new Recipe();
        recipe.setName(recipeRequest.getName());
        return null;
    }

}
