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

});
  detailsControl = new FormControl();
  genders = ['MALE', 'FEMALE'];
  amountOfMeals = [3,4,5];
  goals = ['BULK', 'CUT'];

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
        goal: details.goal

      };
    this.infoService.setDetails(json)
      .toPromise().then(data =>
        {
          this.hasDetailInfo=true;
          console.log(data);
          this.router.navigate(['/user/details']).then(r => console.log(r));

        })
      .catch(error =>
        {

          console.log(error)
      }
      )
  }

  updeteDetails(newDetails) {

    console.log(newDetails);

  }
}
