import {Product} from "../service/payload/Product";
import {Recipe} from "../service/payload/Recipe";

export class PFCK
{
  constructor(p, f, c, k, amountOfMeals) {
    this.protein = Math.round(p/amountOfMeals);
    this.fat = Math.round(f/amountOfMeals);
    this.carbo = Math.round(c/amountOfMeals);
    this.kcal = Math.round(k/amountOfMeals);
  }
  kcal: number;
  protein: number;
  fat: number;
  carbo: number;


  subtractRecipe(recipe: Recipe)
  {
    recipe.products.forEach(product => this.subtractProduct(product));
  }



  subtractProduct(product: Product)
  {
    if(product.metric != 'PC')
    {
      this.kcal -= product.kcal * product.amount / 100;
      this.protein -= product.protein * product.amount / 100;
      this.fat -= product.fat * product.amount / 100;
      this.carbo -= product.carbo * product.amount / 100;
    }else{
      this.kcal -= product.kcal * product.amount;
      this.protein -= product.protein * product.amount;
      this.fat -= product.fat * product.amount ;
      this.carbo -= product.carbo * product.amount;
    }
  }

  addProduct(product: Product) {
    if(product.metric != 'PC')
    {
      this.kcal += product.kcal * product.amount / 100;
      this.protein += product.protein * product.amount / 100;
      this.fat += product.fat * product.amount / 100;
      this.carbo += product.carbo * product.amount / 100;
    }else{
      this.kcal += product.kcal * product.amount;
      this.protein += product.protein * product.amount;
      this.fat += product.fat * product.amount ;
      this.carbo += product.carbo * product.amount;
    }
  }
}
