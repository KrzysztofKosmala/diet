import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Product} from "./payload/Product";
import {ProductsWrapper} from "./payload/ProductsWrapper";

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  constructor(private http: HttpClient) { }
  BASE_URL = 'http://localhost:8080/api/shoppingList';

  createShoppingListIfNeeded()
  {
    return this.http.get(this.BASE_URL+"/init");
  }

  addProducts(products)
  {
    return this.http.post(this.BASE_URL+"/addProducts", products);
  }

  getProducts()
  {
    return this.http.get<ProductsWrapper>(this.BASE_URL);
  }

  updateList(products)
  {
    return this.http.post(this.BASE_URL+"/updateList", products);
  }
}
