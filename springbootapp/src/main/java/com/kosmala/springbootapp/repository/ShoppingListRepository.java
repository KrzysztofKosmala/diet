package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Product;
import com.kosmala.springbootapp.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long>
{
    boolean existsByUserId(Long userId);

    ShoppingList findByUserId(Long id);
}
