import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(private http: HttpClient)
  {

  }
  BASE_URL = 'http://localhost:8080/api/recipe';

  createRecipe(recipe)
  {
    return this.http.post(this.BASE_URL+"/create", recipe);
  }
}
