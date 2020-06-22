import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

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
}
