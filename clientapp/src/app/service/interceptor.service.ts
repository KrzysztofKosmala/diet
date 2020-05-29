import { Injectable } from '@angular/core';
import {HttpInterceptor} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor{

  intercept(req, next) {
    let token = localStorage.getItem('TOKEN');
    let tokenizedReq = req.clone(
      {
      setHeaders: {
        Authorization: 'Bearer ${token}'
      }
    });

    return next.handle(tokenizedReq);
  }


  constructor() { }
}
