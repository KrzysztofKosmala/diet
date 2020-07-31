import { Component, OnInit } from '@angular/core';
import {DailyService} from "../service/daily.service";
import {formatDate} from '@angular/common';
import {DailyPayload} from "./dailyPayload";
import {Product} from "../service/payload/Product";
import {FormControl, FormGroup} from "@angular/forms";
import {ProductService} from "../service/product.service";
import {ProductAmountMealNumber} from "../service/payload/ProductAmountMealNumber";
import {Recipe} from "../service/payload/Recipe";
import {RecipeMultiplierMealNumber} from "../service/payload/RecipeMultiplierMealNumber";
import { PFCK } from './PFCK';
import {RecipeService} from "../service/recipe.service";
import {ShoppingListService} from "../service/shopping-list.service";
import {ProductsWrapper} from "../service/payload/ProductsWrapper";


@Component({
  selector: 'app-daily',
  templateUrl: './daily.component.html',
  styleUrls: ['./daily.component.css'],
})
export class DailyComponent implements OnInit {

  currentDate ;
  isPopupOpened = false;
  constructor(private productService: ProductService, private dailyService: DailyService, private recipeService: RecipeService, private shoppingListService: ShoppingListService)
  {
    this.reloadByDate(localStorage.getItem('CURRENT_DATE'));
    this.currentDate = localStorage.getItem('CURRENT_DATE');
  }

  amountOfMeals;

  //sliders
  eatenKcalValue = 1;
  percentOfProtein = 1;
  percentOfFat = 1;
  percentOfCarbo = 1;
  eatenProteinValue = 1;
  eatenFatValue = 1;
  eatenCarboValue = 1;


  chosenProduct: Product;
  productsInMeals: Map<number, Array<Product>>;
  recipes: Map<number, Array<Recipe>>;
  pfckLeftInEachMeal: Map<number, PFCK>;
  isMealFinishedMap: Map<number, boolean>;

  excludedRecipes: Array<String>;
  currentMealNrForRecipeSearch: number;

  hintedRecipe: Recipe;
  iWantToSearch = false;
  searchedProductListBasedOnInputChange: Array<Product> = [];
  transformMetricMapMetricToWord  = new Map([['ML', 'milliliter'], ['GR', 'gram'], ['PC', 'pieces']]);
  initialData: DailyPayload = null ;
  ngOnInit(): void {
    this.shoppingListService.createShoppingListIfNeeded().toPromise()
      .then(response => console.log(response))
      .catch(err => console.log(err));
  }

  addProductForm = new FormGroup({

    searchForProduct : new FormControl(),
    amountOfProduct : new FormControl(),
  });
  addProductToThisMeal;


  reloadByDate(date)
  {
    this.eatenKcalValue = 0;
    this.percentOfProtein = 0;
    this.percentOfFat = 0;
    this.percentOfCarbo = 0;
    this.eatenProteinValue = 0;
    this.eatenFatValue = 0;
    this.eatenCarboValue = 0;

    this.dailyService.getDailyByDate(date.toString()).toPromise().then(response=>
    {
      console.log(response)
      this.productsInMeals = new Map<number, Array<Product>>();
      this.recipes = new Map<number, Array<Recipe>>();
      this.pfckLeftInEachMeal =  new Map<number, PFCK>();
      this.isMealFinishedMap = new Map<number, boolean>();
      this.excludedRecipes = new Array<String>();
      for (var _i = 1; _i < response.amountOfMeals + 1; _i++)
      {
        this.productsInMeals.set(_i, new Array<Product>());
        this.recipes.set(_i, new Array<Recipe>());
        this.pfckLeftInEachMeal.set(_i, new PFCK(response.protein, response.fat, response.carbo, response.kcal, response.amountOfMeals));
        this.isMealFinishedMap.set(_i, false);
      }

      if(response.productAmountMealNumbers != null)
      {
        response.productAmountMealNumbers.forEach(e => {

          this.productsInMeals.get(e.mealNumber).push(e.product);
          this.pfckLeftInEachMeal.get(e.mealNumber).subtractProduct(e.product);
         this.addProductToStatistics(e.product);

        });
      }
      console.log(this.pfckLeftInEachMeal);
      if(response.recipesMultiplierMealNumber != null)
      response.recipesMultiplierMealNumber.forEach(e =>
      {
        this.addRecipeToMeal(e.mealNumber, e.recipe);
        this.pfckLeftInEachMeal.get(e.mealNumber).subtractRecipe(e.recipe);
      });
      this.amountOfMeals = Array(response.amountOfMeals).fill(0).map((x,i)=>i+1);
      this.initialData = response;

      console.log(response)
    }).then(   e =>
      {
        if(this.initialData != null)
        {
          console.log(e);
          this.percentOfProtein = (this.initialData.protein * 4) * 100 / this.initialData.kcal;
          this.percentOfFat = (this.initialData.fat * 9) * 100 / this.initialData.kcal;
          this.percentOfCarbo = (this.initialData.carbo * 4) * 100 / this.initialData.kcal;
        }

      }
    ).catch(err => {
      console.log(err);
    })
    console.log(this.recipes)
    console.log(this.productsInMeals)
    console.log(this.isMealFinishedMap)
    console.log(this.excludedRecipes)

  }

  addRecipeToMeal(mealNr, recipe)
  {
    this.excludedRecipes.push(recipe.name);
    this.recipes.get(mealNr).push(recipe);
    console.log(this.recipes)
  }
  addRecipeWithProductsToMeal(mealNr, recipe)
  {
    this.excludedRecipes.push(recipe.name);
    this.recipes.get(mealNr).push(recipe);
        recipe.products.forEach(product => {
          this.productsInMeals.get(mealNr).push(product);
          this.addProductToStatistics(product);
          this.pfckLeftInEachMeal.get(mealNr).subtractProduct(product);
        });
    this.isPopupOpened = false;
  }

  addProductToStatistics(product)
  {
    if(product.metric != 'PC')
    {
      this.eatenKcalValue += product.kcal * product.amount / 100;
      this.eatenProteinValue += product.protein * product.amount /  100;
      this.eatenFatValue += product.fat * product.amount / 100;
      this.eatenCarboValue += product.carbo * product.amount / 100;
    }else{
      this.eatenKcalValue += product.kcal * product.amount;
      this.eatenProteinValue += product.protein * product.amount;
      this.eatenFatValue += product.fat * product.amount;
      this.eatenCarboValue += product.carbo * product.amount;
    }
    this.eatenCarboValue =  Math.round(this.eatenCarboValue*10) / 10;
    this.eatenFatValue = Math.round(this.eatenFatValue*10) / 10;
    this.eatenKcalValue= Math.round(this.eatenKcalValue*10) / 10;
    this.eatenProteinValue= Math.round(this.eatenProteinValue*10) / 10;
  }

  removeProductFromStatistics(product)
  {
    if(product.metric != 'PC')
    {
      this.eatenKcalValue -= product.kcal * product.amount / 100;
      this.eatenProteinValue -= product.protein * product.amount /  100;
      this.eatenFatValue -= product.fat * product.amount / 100;
      this.eatenCarboValue -= product.carbo * product.amount / 100;
    }else{
      this.eatenKcalValue -= product.kcal * product.amount;
      this.eatenProteinValue -= product.protein * product.amount;
      this.eatenFatValue -= product.fat * product.amount;
      this.eatenCarboValue -= product.carbo * product.amount;
    }
    this.eatenCarboValue =  Math.round(this.eatenCarboValue*10) / 10;
    this.eatenFatValue = Math.round(this.eatenFatValue*10) / 10;
    this.eatenKcalValue= Math.round(this.eatenKcalValue*10) / 10;
    this.eatenProteinValue= Math.round(this.eatenProteinValue*10) / 10;
  }

  removeProduct(meal, iterator , product) {

    let i =0;
    this.productsInMeals.get(meal).forEach(productToRemove =>
    {
      console.log(productToRemove.amount)
      if(productToRemove.name === product.name && productToRemove.amount === product.amount)
      {
        this.productsInMeals.get(meal).splice(i, 1);
        this.removeProductFromStatistics(product);
        this.pfckLeftInEachMeal.get(meal).addProduct(product);
      }
      i++;
    });

  }

  decrementDay() {
    this.currentDate = formatDate(new Date(localStorage.getItem('CURRENT_DATE') ).getTime() - 1*24*60*60*1000, 'yyyy-MM-dd', 'en');
    localStorage.setItem('CURRENT_DATE', this.currentDate);
    this.reloadByDate(this.currentDate);
  }

  incrementDay() {
    let currentDate1 = formatDate(new Date(localStorage.getItem('CURRENT_DATE') ).getTime() + 24*60*60*1000, 'yyyy-MM-dd', 'en');
    if(currentDate1 != formatDate(new Date().getTime() + 24*60*60*1000, 'yyyy-MM-dd', 'en'))
    {
      this.currentDate = currentDate1;
      localStorage.setItem('CURRENT_DATE', this.currentDate);
      this.reloadByDate(this.currentDate);
    }
  }

  onSelection(e, v) {
    this.chosenProduct = null;

    this.chosenProduct = e.option.value;
    console.log(e.option.value)
  }

  pingingSearch(event) {
    this.productService.getProductByPartOfName(event.target.value).toPromise().then(response=>
      {
        this.searchedProductListBasedOnInputChange = [];
        console.log(response);
        response.forEach(element => this.searchedProductListBasedOnInputChange.push(element));
        console.log(this.searchedProductListBasedOnInputChange)
      }

    ).catch(err => {
      this.searchedProductListBasedOnInputChange = [];
      var errorProduct = new Product();
      errorProduct.name = err.error.message;
      this.searchedProductListBasedOnInputChange.push(errorProduct);
    })
  }

  addProductToMeal(value) {
    this.chosenProduct.amount = value.amountOfProduct;
    this.productsInMeals.get(this.addProductToThisMeal).push(this.chosenProduct);
    this.addProductForm.reset();
    this.searchedProductListBasedOnInputChange = new Array<Product>();

    this.addProductToStatistics(this.chosenProduct);

    this.pfckLeftInEachMeal.get(this.addProductToThisMeal).subtractProduct(this.chosenProduct);
  }

  save() {


      let daily = new DailyPayload();


      for (let [key, products] of this.productsInMeals)
      {
        for(let product of products)
        {
          let productAmountMealNumber = new ProductAmountMealNumber();
          productAmountMealNumber.mealNumber = key;
          productAmountMealNumber.product = product;
          daily.productAmountMealNumbers.push(productAmountMealNumber);
        }
      }

    for (let [key, recipes] of this.recipes)
    {
      for(let recipe of recipes)
      {
        let recipeMultiplierMealNumber = new RecipeMultiplierMealNumber();
        recipeMultiplierMealNumber.recipe = recipe;
        recipeMultiplierMealNumber.recipe.multiplier = recipe.multiplier;
        recipeMultiplierMealNumber.mealNumber = key;
        daily.recipesMultiplierMealNumber.push(recipeMultiplierMealNumber);
      }
    }
    console.log(daily);
      this.dailyService.updateDaily(daily, localStorage.getItem('CURRENT_DATE')).toPromise().then(data =>
      {

        console.log(data);
      }).catch(error =>
      {

        console.log(error)
      });

      console.log(daily)
  }

  countKcalInMeal(mealNr)
  {
    let sum = 0;

      this.productsInMeals.get(mealNr).forEach(a => {
        if(a.metric != 'PC')
        sum += (a.kcal * a.amount) /100;
        else{
          sum += (a.kcal * a.amount);
        }
      });
    return sum;
  }

  countProteinInMeal(mealNr) {
    let sum = 0;

    this.productsInMeals.get(mealNr).forEach(a => {
      if(a.metric != 'PC')
        sum += (a.protein * a.amount) /100;
      else{
        sum += (a.protein * a.amount);
      }
    });
    return sum;
  }

  countFatInMeal(mealNr) {
    let sum = 0;

    this.productsInMeals.get(mealNr).forEach(a => {
      if(a.metric != 'PC')
        sum += (a.fat * a.amount) /100;
      else{
        sum += (a.fat * a.amount);
      }
    });
    return sum;
  }

  countCarboInMeal(mealNr) {
    let sum = 0;

    this.productsInMeals.get(mealNr).forEach(a => {
      if(a.metric != 'PC')
        sum += (a.carbo * a.amount) /100;
      else{
        sum += (a.carbo * a.amount);
      }
    });
    return sum;
  }

  removeRecipe(meal, iterator , recipe) {
    this.recipes.get(meal).splice(iterator, 1);
    recipe.products.forEach(product =>
    {
      console.log("removeRecipe")
      console.log(product)

      let i =0;
      this.productsInMeals.get(meal).forEach(productToRemove =>
      {
        console.log(productToRemove.amount)
        if(productToRemove.name === product.name && productToRemove.amount === product.amount)
        {
          this.productsInMeals.get(meal).splice(i, 1);
          this.removeProductFromStatistics(product);
          this.pfckLeftInEachMeal.get(meal).addProduct(product);
        }
        i++;
      });


      //this.pfckLeftInEachMeal.get(meal).addProduct(product);
    });
  }

  openPopupAndFindMeal(i)
  {

    this.currentMealNrForRecipeSearch = i;
    this.findMeal(i);

    this.isPopupOpened = true;

  }

  findMeal(i)
  {
    let neededPfck = this.pfckLeftInEachMeal.get(i);
    var json =
      {
        kcal: neededPfck.kcal,
        protein: neededPfck.protein,
        carbo: neededPfck.carbo,
        fat: neededPfck.fat
      };
    this.recipeService.findRecipeBasedOnNeededPFCK(json).toPromise()
      .then(response => this.hintedRecipe = response)
      .catch(error =>
      {
        console.log(error)
      });
  }

  finishThisMeal(mealNumber)
  {

    this.isMealFinishedMap.set(mealNumber, true);
    let pfckLeftInMealToFinish = this.pfckLeftInEachMeal.get(mealNumber);
    let notFinished = [];
    this.pfckLeftInEachMeal.forEach((value: PFCK, key: number) =>
    {
      if(!this.isMealFinishedMap.get(key))
        notFinished.push(key);
    });

    console.log(notFinished);
    notFinished.forEach(notFinishedMealNr =>
    {
      this.pfckLeftInEachMeal.get(notFinishedMealNr).protein += Math.round(pfckLeftInMealToFinish.protein / notFinished.length);
      this.pfckLeftInEachMeal.get(notFinishedMealNr).fat += Math.round(pfckLeftInMealToFinish.fat / notFinished.length);
      this.pfckLeftInEachMeal.get(notFinishedMealNr).carbo += Math.round(pfckLeftInMealToFinish.carbo / notFinished.length);
      this.pfckLeftInEachMeal.get(notFinishedMealNr).kcal +=  Math.round(pfckLeftInMealToFinish.kcal / notFinished.length);
    });

    console.log(this.pfckLeftInEachMeal);
  }

  sendIngredientsToShoppingList() {
    console.log(this.recipes)
    let productsWrapper = new ProductsWrapper();
    let productsToShoppingList: Array<Product> = [];
    for (let [key, recipes] of this.recipes)
    {
      recipes.forEach( recipe =>
      {
        recipe.products.forEach(product =>
        {
          productsToShoppingList.push(product);
        })
      })
    }
    productsWrapper.products = productsToShoppingList;

    console.log(productsWrapper)
    this.shoppingListService.addProducts(productsWrapper).toPromise()
      .then(response => console.log(response)).catch(err => console.log(err));

  }
}
