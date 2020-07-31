package com.kosmala.springbootapp.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@SuppressWarnings("serial")
@Embeddable
public class ShoppingListProductAmountId implements Serializable
{
    private Long shoppingListId;
    private Long productId;
}
