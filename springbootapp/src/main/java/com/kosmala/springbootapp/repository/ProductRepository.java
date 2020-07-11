package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    boolean existsByName(String name);
    Product findByName(String name);
    List<Product> findByNameContains(String nameLike);
}
