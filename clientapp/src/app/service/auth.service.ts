import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import {map, filter, switchMap, catchError} from 'rxjs/operators';
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {JwtHelper} from "angular2-jwt";


@Injectable()
export class AuthService {
  constructor(private http: Http) {
  }
  BASE_URL = 'http://localhost:8080/api/auth';


  login(credentials)
  {
    return this.http.post(this.BASE_URL+'/login', credentials).pipe(map(response => response.json()))
  }

  signup(userDetail)
  {
     return this.http.post(this.BASE_URL+'/signup',
      userDetail).pipe(map(response => response.json()))
  }

  logout() {
    localStorage.removeItem('TOKEN');
    localStorage.removeItem('CURRENT_DATE');
    this.http.get(this.BASE_URL+'/logout')
  }


  get currentUser()
  {
    let token = localStorage.getItem('TOKEN');
    if(!token) return null;
      return new JwtHelper().decodeToken(token);
  }

  isLoggedIn() {

    return false;
  }
}
