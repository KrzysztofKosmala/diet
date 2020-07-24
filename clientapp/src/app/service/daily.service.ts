import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../payload/Product";
import {DailyPayload} from "../daily/dailyPayload";

@Injectable({
  providedIn: 'root'
})
export class DailyService
{

  constructor(private http: HttpClient) { }
  BASE_URL = 'http://localhost:8080/api/daily';

  getDailyByDate(date)
  {
    const params = new HttpParams()
      .set('date', date);
    return this.http.get<DailyPayload>(this.BASE_URL, {params});
  }

  updateDaily(newDaily, date)
  {
    const params = new HttpParams()
    .set('date', date);
    return this.http.post(this.BASE_URL+"/update", newDaily, {params});
  }
}
