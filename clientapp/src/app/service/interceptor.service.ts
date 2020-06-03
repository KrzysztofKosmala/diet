import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor{

  intercept(req: HttpRequest<any>,
            next: HttpHandler) : Observable<HttpEvent<any>>{
    console.log("interceptor");
    let token = localStorage.getItem('TOKEN');
    let tokenizedReq;
    if(token)
    {  tokenizedReq = req.clone(
      {
      headers: req.headers.set(
        "Authorization", "Bearer " + token)

    });}else{
      console.log("empty");
      return  next.handle(req);
    }

    console.log(tokenizedReq);
    return next.handle(tokenizedReq);
  }


  constructor() { }
}
