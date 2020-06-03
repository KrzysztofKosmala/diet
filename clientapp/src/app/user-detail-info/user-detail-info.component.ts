import { Component, OnInit } from '@angular/core';
import {UserDetailInfoServiceService} from "../service/user-detail-info-service.service";
import {UserDetails} from "../payload/user-details";

@Component({
  selector: 'app-user-detail-info',
  templateUrl: './user-detail-info.component.html',
  styleUrls: ['./user-detail-info.component.css']
})
export class  UserDetailInfoComponent implements OnInit {

  constructor(private infoService: UserDetailInfoServiceService) {console.log("tutej"); this.getDetails();}

  details: UserDetails;
  hasDetailInfo: boolean;


  getDetails()
  {
    return this.infoService.getDetails()
      .then(response => {
        console.log(response);
        this.hasDetailInfo=true;
      } )
      ,(error) => {

      this.hasDetailInfo = false;
    }
  }
  ni()
  {

     this.infoService.ni().then(data=>console.log(data));
  }

  ngOnInit(): void {
  }

}
