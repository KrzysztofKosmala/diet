import { Injectable } from '@angular/core';
import {Http} from "@angular/http";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDetails} from "./payload/user-details";

@Injectable({
  providedIn: 'root'
})
export class UserDetailInfoServiceService {

  constructor(private http: HttpClient)
  {

  }
  BASE_URL = 'http://localhost:8080/api/users';

  getDetails(): Observable<UserDetails>
  {
    return this.http.get<UserDetails>(this.BASE_URL+'/details');
  }

  setDetails(details)
  {
    return this.http.post(this.BASE_URL+"/details", details);
  }

  updateDetails(details)
  {
    return this.http.post(this.BASE_URL+"/updateDetails", details);
  }

}
