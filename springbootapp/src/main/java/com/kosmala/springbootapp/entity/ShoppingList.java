package com.kosmala.springbootapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_list")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ShoppingList
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingList", fetch=FetchType.EAGER)
    private Set<ShoppingListProductAmount> products = new HashSet<>();

    @OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name = "user_fk", referencedColumnName = "id")
    User user;
}
