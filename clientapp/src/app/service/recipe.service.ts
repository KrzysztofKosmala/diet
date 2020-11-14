import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {PFCK} from "../daily/PFCK";
import {DailyPayload} from "../daily/dailyPayload";
import {Recipe} from "./payload/Recipe";
import set = Reflect.set;

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(private http: HttpClient) {}
  BASE_URL = 'http://localhost:8080/api/recipe';

  createRecipe(recipe)
  {
    return this.http.post(this.BASE_URL+"/create", recipe);
  }

  findRecipeBasedOnNeededPFCK(pfck)
  {
    const params = new HttpParams()
      .set('protein', pfck.protein)
      .set('fat', pfck.fat)
      .set('carbo', pfck.carbo)
      .set('kcal', pfck.kcal)
      .set('mealNr', pfck.mealNr)
      .set('amountOfMeals', pfck.amountOfMeals)
      .set('excluded', pfck.excluded);
    return this.http.get<Recipe>(this.BASE_URL+"/hintTheRecipe",{params});
  }
}
