package com.kosmala.springbootapp.controller;

import com.kosmala.springbootapp.entity.Role;
import com.kosmala.springbootapp.entity.RoleName;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.exception.AppException;
import com.kosmala.springbootapp.payload.JwtAuthenticationResponse;
import com.kosmala.springbootapp.payload.LoginRequest;
import com.kosmala.springbootapp.payload.CustomResponse;
import com.kosmala.springbootapp.payload.SignUpRequest;
import com.kosmala.springbootapp.repository.RoleRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import com.kosmala.springbootapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {

        if(!userRepository.existsByUsername(loginRequest.getUsernameOrEmail())) {
            return new ResponseEntity(new CustomResponse(false, "There is no such user in the database"),
                    HttpStatus.BAD_REQUEST);
        }

        if(!userRepository.existsByEmail(loginRequest.getUsernameOrEmail())) {
            return new ResponseEntity(new CustomResponse(false, "There is no such user in the database"),
                    HttpStatus.BAD_REQUEST);
        }

        final UsernamePasswordAuthenticationToken authentication1 = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(
                authentication1
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
     
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new CustomResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new CustomResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        return ResponseEntity.ok(new CustomResponse(true, "User registered successfully"));
    }
}
