package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.ShoppingList;
import com.kosmala.springbootapp.entity.ShoppingListProductAmount;
import com.kosmala.springbootapp.entity.ShoppingListProductAmountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListProductAmountRepository extends JpaRepository<ShoppingListProductAmount, ShoppingListProductAmountId>
{

}
