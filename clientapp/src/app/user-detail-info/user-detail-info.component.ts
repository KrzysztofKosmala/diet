import { Component, OnInit } from '@angular/core';
import {UserDetailInfoServiceService} from "../service/user-detail-info-service.service";
import {UserDetails} from "../payload/user-details";
import {HttpErrorResponse} from "@angular/common/http";
import {FormControl, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-detail-info',
  templateUrl: './user-detail-info.component.html',
  styleUrls: ['./user-detail-info.component.css']
})
export class  UserDetailInfoComponent implements OnInit {

  constructor(private router: Router,
              private infoService: UserDetailInfoServiceService)
  { }

  details: UserDetails;
  errMes: String;
  hasDetailInfo: boolean;



  getDetails()
  {
    return this.infoService.getDetails().toPromise()
      .then(response => {
        this.details = response;
        console.log(response);
        this.hasDetailInfo=true;
      } )
      .catch(error => {
        if (error instanceof HttpErrorResponse) {
          this.errMes = error.error.message;
        }

        this.hasDetailInfo = false;
      })
  }
  form = new FormGroup({

    gender : new FormControl(),
    amount_of_meals : new FormControl(),
    weight : new FormControl(),
    age : new FormControl(),
    height : new FormControl(),
    goal : new FormControl(),
    activity : new FormControl(),
    caloric_intake : new FormControl(),
    protein : new FormControl(),
    fat : new FormControl(),
    carbo : new FormControl(),

});
  detailsControl = new FormControl();
  genders = ['MALE', 'FEMALE'];
  amountOfMeals = [3,4,5];
  goals = ['BULK', 'CUT'];
  myMap = new Map();

  activities = new Map(
           [["1.2", "1.2 - Lack of Activity"],
                  ["1.3", "1.3 - low activity (sedentary work and 1-2 workouts per week)"],
                  ["1.6", "1.6 - average activity (sedentary work and training 3-4 times a week)"],
                  ["1.8", "1.8 - high activity (physical work and 3-4 workouts per week)"],
                  ["2.0", "2.0 - very high activity (professional athletes, people who train every day)"]]);

  ngOnInit(): void {
    this.getDetails();
  }

  setDetail(details) {
    var json =
      {
        gender: details.gender,
        amount_of_meals: details.amount_of_meals,
        weight: details.weight,
        age: details.age,
        height: details.height,
        activity: details.activity,
        caloric_intake: details.caloric_intake,
        goal: details.goal

      };
    this.infoService.setDetails(json)
      .toPromise().then(data =>
        {
          this.hasDetailInfo=true;
          console.log(data);
          this.router.navigate(['/user']).then(r => console.log(r));

        })
      .catch(error =>
        {

          console.log(error)
      }
      )
  }

  updeteDetails(newDetails) {

    /*for now*/
    var json =
      {
        gender: newDetails.gender,
        amount_of_meals: newDetails.amount_of_meals,
        caloric_intake: newDetails.caloric_intake,
        weight: newDetails.weight,
        age: newDetails.age,
        height: newDetails.height,
        activity: newDetails.activity,
        goal: newDetails.goal,
        protein: newDetails.protein,
        fat: newDetails.fat,
        carbo: newDetails.carbo

      };
    this.infoService.updateDetails(json)
      .toPromise().then(data =>
    {
      this.hasDetailInfo=true;
      console.log(data);
      this.router.navigate(['/user']).then(r => console.log(r));

    })
      .catch(error =>
        {
          this.router.navigate(['/user']).then(r => console.log(r));
          console.log(error)
        }
      )
  }
}
