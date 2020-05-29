package com.kosmala.springbootapp.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponse
{
    private Boolean success;
    private String message;
}
