package com.kosmala.springbootapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public UserController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @RequestMapping("/findAll")
    public ResponseEntity getByUsername() throws JsonProcessingException
    {

        return ResponseEntity.ok(objectMapper.writeValueAsString(userRepository.findAll()));

    }
}