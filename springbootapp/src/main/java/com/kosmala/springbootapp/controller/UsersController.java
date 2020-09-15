package com.kosmala.springbootapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmala.springbootapp.entity.DetailedUserInfo;
import com.kosmala.springbootapp.entity.GenderName;
import com.kosmala.springbootapp.entity.GoalName;
import com.kosmala.springbootapp.entity.User;
import com.kosmala.springbootapp.exception.ResourceNotFoundException;
import com.kosmala.springbootapp.helpers.countCaloricIntake.ICaloricIntake;
import com.kosmala.springbootapp.helpers.countCaloricIntake.IMacroRatio;
import com.kosmala.springbootapp.helpers.countCaloricIntake.Ratio;
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
    ICaloricIntake countCaloricIntake;

    @Autowired
    IMacroRatio countMacroRatio;

    @Autowired
    DetailedUserInfoRepository detailedUserInfoRepository;

    @PostMapping("/details")
    public ResponseEntity attachUd(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody DetailedUserInfoRequest userDetailsRequest) throws JsonProcessingException
    {
        //find user in db
        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);

        //convert req class to detail user info
        DetailedUserInfo detailedUserInfo = detailedUserInfoRequestToDetailedUserInfo(userDetailsRequest);

        //count caloric intake
        int caloricIntake = countCaloricIntake.count(detailedUserInfo);

        //set caloric intake
        detailedUserInfo.setCaloric_intake(caloricIntake);

        //count ratio
        Ratio ratio = countMacroRatio.countRatio(detailedUserInfo.getGender(), caloricIntake, detailedUserInfo.getWeight());

        //set BTW
        detailedUserInfo.setProtein(ratio.getProtein());
        detailedUserInfo.setFat(ratio.getFat());
        detailedUserInfo.setCarbo(ratio.getCarbo());

        detailedUserInfoRepository.save(detailedUserInfo);

        assert user != null;
        user.setDetailedUserInfo(detailedUserInfo);

        userRepository.save(user);

        return ResponseEntity.ok(new CustomResponse(true, "Added user details successfully"));
    }

    @PostMapping("/updateDetails")
    public ResponseEntity updateUd(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody DetailedUserInfoRequest userDetailsRequest) throws JsonProcessingException
    {

        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);

        assert user != null;
        DetailedUserInfo detailedUserInfoTuUpdate = detailedUserInfoRepository.getOne(user.getDetailedUserInfo().getId());


        detailedUserInfoTuUpdate.setAge(userDetailsRequest.getAge());
        detailedUserInfoTuUpdate.setAmount_of_meals(userDetailsRequest.getAmount_of_meals());

        if(userDetailsRequest.getCaloric_intake() != detailedUserInfoTuUpdate.getCaloric_intake()
                && checkIfMacroEqualsKcal(userDetailsRequest.getCaloric_intake(), userDetailsRequest.getProtein(), userDetailsRequest.getFat(), userDetailsRequest.getCarbo()))
            return new ResponseEntity(new CustomResponse(false, "Please check if sum of macro equals with new caloric intake!"),
                    HttpStatus.BAD_REQUEST);


        //add checking if counting is needed


        detailedUserInfoTuUpdate.setGender(GenderName.valueOf(userDetailsRequest.getGender()));
        detailedUserInfoTuUpdate.setWeight(userDetailsRequest.getWeight());
        detailedUserInfoTuUpdate.setHeight(userDetailsRequest.getHeight());
        detailedUserInfoTuUpdate.setActivity(userDetailsRequest.getActivity());
        detailedUserInfoTuUpdate.setGoal(GoalName.valueOf(userDetailsRequest.getGoal()));

        int caloricIntake = countCaloricIntake.count(detailedUserInfoTuUpdate);

        detailedUserInfoTuUpdate.setCaloric_intake(caloricIntake);

        //count ratio
        Ratio ratio = countMacroRatio.countRatio(detailedUserInfoTuUpdate.getGender(), caloricIntake, detailedUserInfoTuUpdate.getWeight());

        detailedUserInfoTuUpdate.setProtein(ratio.getProtein());
        detailedUserInfoTuUpdate.setFat(ratio.getFat());
        detailedUserInfoTuUpdate.setCarbo(ratio.getCarbo());
        detailedUserInfoRepository.save(detailedUserInfoTuUpdate);

        return ResponseEntity.ok(new CustomResponse(true, "User detail info has been updated successfully"));
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
        detailedUserInfo.setActivity(detailedUserInfoRequest.getActivity());
        detailedUserInfo.setGoal(GoalName.valueOf(detailedUserInfoRequest.getGoal()));

        return detailedUserInfo;
    }

    private boolean checkIfMacroEqualsKcal(int caloricIntake, int protein, int fat, int carbo)
    {
        return caloricIntake == protein*4 + fat*9 + carbo*4;
    }
}
