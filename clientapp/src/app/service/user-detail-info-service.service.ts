import { Injectable } from '@angular/core';
import {Http} from "@angular/http";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserDetailInfoServiceService {

  constructor(private http: HttpClient)
  {

  }
  BASE_URL = 'http://localhost:8080/api/users';
  getDetails()
  {
    return this.http.get(this.BASE_URL+'/details').toPromise()

  }

  ni() {

   return   this.http.get(this.BASE_URL + '/ni', {}).toPromise()

  }
}
