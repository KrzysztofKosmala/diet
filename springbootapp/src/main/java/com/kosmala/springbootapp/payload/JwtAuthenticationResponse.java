package com.kosmala.springbootapp.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtAuthenticationResponse
{
    private String accessToken;
    private final String tokenType = "Bearer";
}
