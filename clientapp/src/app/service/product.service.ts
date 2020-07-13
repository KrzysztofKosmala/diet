import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../payload/Product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }
  BASE_URL = 'http://localhost:8080/api/products';

  createProduct(product)
  {
    return this.http.post(this.BASE_URL+"/create", product);
  }

  getProductByName(name): Observable<Product>
  {
    const params = new HttpParams()
      .set('name', name);
    return this.http.get<Product>(this.BASE_URL, {params});
  }

  getProductByPartOfName(name): Observable<Array<Product>>
  {
    const params = new HttpParams()
      .set('nameLike', name);
    return this.http.get<Array<Product>>(this.BASE_URL+"/nameLike", {params});
  }
}
