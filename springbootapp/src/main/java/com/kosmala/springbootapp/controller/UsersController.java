package com.kosmala.springbootapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.GenderName;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.exception.ResourceNotFoundException;
import com.kosmala.springbootapp.payload.DetailedUserInfoRequest;
import com.kosmala.springbootapp.repository.DetailedUserInfoRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController
{
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DetailedUserInfoRepository detailedUserInfoRepository;

    @PostMapping("/addDetails")
    public ResponseEntity attachUd(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody DetailedUserInfoRequest userDetailsRequest) throws JsonProcessingException
    {

        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);

        DetailedUserInfo detailedUserInfo = detailedUserInfoRequestToDetailedUserInfo(userDetailsRequest);

        detailedUserInfoRepository.save(detailedUserInfo);

        user.setDetailedUserInfo(detailedUserInfo);

        userRepository.save(user);

        return ResponseEntity.ok(objectMapper.writeValueAsString(userRepository.findByUsernameOrEmail(currentUser.getEmail(),currentUser.getEmail())));
    }

    @GetMapping("/getDetails")
    public ResponseEntity getDetails(@AuthenticationPrincipal UserPrincipal currentUser)
    {
        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);
        return ResponseEntity.ok(user.getDetailedUserInfo());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity getUserProfile(@PathVariable(value = "username") String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return ResponseEntity.ok(user.getDetailedUserInfo());
    }

    private DetailedUserInfo detailedUserInfoRequestToDetailedUserInfo(DetailedUserInfoRequest detailedUserInfoRequest)
    {
        DetailedUserInfo detailedUserInfo = new DetailedUserInfo();

        detailedUserInfo.setAge(detailedUserInfoRequest.getAge());
        detailedUserInfo.setAmount_of_meals(detailedUserInfoRequest.getAmount_of_meals());
        detailedUserInfo.setCaloric_intake(detailedUserInfoRequest.getCaloric_intake());
        detailedUserInfo.setGender(GenderName.valueOf(detailedUserInfoRequest.getGender()));
        detailedUserInfo.setWeight(detailedUserInfoRequest.getWeight());

        return detailedUserInfo;
    }
}
