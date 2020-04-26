package com.kosmala.springbootapp.security;


import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtTokenProvider
{
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(Authentication authentication)
    {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("email", userPrincipal.getEmail())
                .claim("name", userPrincipal.getName())
                .claim("forSureItsNotPassword", userPrincipal.getPassword())
                .claim("username", userPrincipal.getUsername())
                .claim("authorities",
                        userPrincipal.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UserDetails generateUserDetailsBasedOnToken(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = ((List<Map<String, String>> ) claims.get("authorities")).stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());

        return new UserPrincipal(
                Long.parseLong(claims.getSubject()),
                (String) claims.get("name"),
                (String) claims.get("username"),
                (String) claims.get("email"),
                (String) claims.get("forSureItsNotPassword"),
                simpleGrantedAuthorities);
    }
    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
