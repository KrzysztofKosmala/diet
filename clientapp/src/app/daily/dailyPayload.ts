import {Product} from "../payload/Product";
import {ProductAmountMealNumber} from "./ProductAmountMealNumber";

export class DailyPayload
{
  date: String;
  amountOfMeals: number;
  kcal: number;
  protein: number;
  fat: number;
  carbo: number;
  productAmountMealNumbers: Array<ProductAmountMealNumber>
}
