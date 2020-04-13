package com.kosmala.springbootapp;

import com.kosmala.springbootapp.entity.Role;
import com.kosmala.springbootapp.entity.RoleName;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.exception.AppException;
import com.kosmala.springbootapp.repository.RoleRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
public class Home
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }


    @GetMapping("/createMe")
    public String create()
    {
        User user = new User();
        user.setEmail("eksmkrz@ericsson.com");
        user.setUsername("Kosmala");
        user.setPassword("password");
        user.setName("kosmita");

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));;

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        return ("<h1>Welcome</h1>");
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }
}
