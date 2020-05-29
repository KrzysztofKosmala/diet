import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import {map, filter, switchMap, catchError} from 'rxjs/operators';
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";

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
     return this.http.post('http://localhost:8080/api/auth/signup',
      userDetail).pipe(map(response => response.json()))
  }

  logout() {
  }


  isLoggedIn() {
    return false;
  }
}
