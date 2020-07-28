import {Product} from "../payload/Product";
import {ProductAmountMealNumber} from "../payload/ProductAmountMealNumber";
import {RecipeMultiplierMealNumber} from "../payload/RecipeMultiplierMealNumber";

export class DailyPayload
{
  date: String;
  amountOfMeals: number;
  kcal: number;
  protein: number;
  fat: number;
  carbo: number;
  productAmountMealNumbers: Array<ProductAmountMealNumber>;
  recipesMultiplierMealNumber: Array<RecipeMultiplierMealNumber>;
}
