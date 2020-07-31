package com.kosmala.springbootapp.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductsWrapper
{
    private List<ProductPayload> products = new ArrayList<>();


    public ProductsWrapper(List<ProductPayload> productPayloads)
    {
        products = productPayloads;
    }
}
