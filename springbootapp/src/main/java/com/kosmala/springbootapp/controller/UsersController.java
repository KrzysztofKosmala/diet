package com.kosmala.springbootapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.GenderName;
import com.kosmala.springbootapp.entity.GoalName;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.exception.ResourceNotFoundException;
import com.kosmala.springbootapp.payload.CustomResponse;
import com.kosmala.springbootapp.payload.DetailedUserInfoRequest;
import com.kosmala.springbootapp.repository.DetailedUserInfoRepository;
import com.kosmala.springbootapp.repository.UserRepository;
import com.kosmala.springbootapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/details")
    public ResponseEntity attachUd(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody DetailedUserInfoRequest userDetailsRequest) throws JsonProcessingException
    {

        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);

        DetailedUserInfo detailedUserInfo = detailedUserInfoRequestToDetailedUserInfo(userDetailsRequest);

        //COUNT CALORIC INTAKE

        detailedUserInfoRepository.save(detailedUserInfo);

        user.setDetailedUserInfo(detailedUserInfo);

        userRepository.save(user);

        return ResponseEntity.ok(new CustomResponse(true, "Added user details successfully"));
    }

    @GetMapping("/details")
    public ResponseEntity getDetails(@AuthenticationPrincipal UserPrincipal currentUser)
    {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        if(user.getDetailedUserInfo() == null)
            return new ResponseEntity(new CustomResponse(false, "User doesnt have detail info!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(user.getDetailedUserInfo());
    }

    private DetailedUserInfo detailedUserInfoRequestToDetailedUserInfo(DetailedUserInfoRequest detailedUserInfoRequest)
    {
        DetailedUserInfo detailedUserInfo = new DetailedUserInfo();

        detailedUserInfo.setAge(detailedUserInfoRequest.getAge());
        detailedUserInfo.setAmount_of_meals(detailedUserInfoRequest.getAmount_of_meals());
        detailedUserInfo.setGender(GenderName.valueOf(detailedUserInfoRequest.getGender()));
        detailedUserInfo.setWeight(detailedUserInfoRequest.getWeight());
        detailedUserInfo.setHeight(detailedUserInfoRequest.getHeight());
        detailedUserInfo.setGoal(GoalName.valueOf(detailedUserInfoRequest.getGoal()));

        return detailedUserInfo;
    }
}
