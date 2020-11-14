import {Product} from "../service/payload/Product";
import {ProductAmountMealNumber} from "../service/payload/ProductAmountMealNumber";
import {RecipeMultiplierMealNumber} from "../service/payload/RecipeMultiplierMealNumber";

export class DailyPayload
{
  date: String;
  amountOfMeals: number;
  kcal: number;
  protein: number;
  fat: number;
  carbo: number;
  productAmountMealNumbers= new Array<ProductAmountMealNumber>();
  recipesMultiplierMealNumber= new  Array<RecipeMultiplierMealNumber>();
  excludedRecipesFromDayBefore= new Array<String>();
}
