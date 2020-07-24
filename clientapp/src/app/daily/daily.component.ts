import { Component, OnInit } from '@angular/core';
import {DailyService} from "../service/daily.service";
import {formatDate} from '@angular/common';
import {DailyPayload} from "./dailyPayload";
import {Product} from "../payload/Product";
import {FormControl, FormGroup} from "@angular/forms";
import {ProductService} from "../service/product.service";
import {ProductAmountMealNumber} from "./ProductAmountMealNumber";


@Component({
  selector: 'app-daily',
  templateUrl: './daily.component.html',
  styleUrls: ['./daily.component.css'],
})
export class DailyComponent implements OnInit {

  currentDate ;
  constructor(private productService: ProductService, private dailyService: DailyService) {
    this.reloadByDate(localStorage.getItem('CURRENT_DATE'));
    this.currentDate = localStorage.getItem('CURRENT_DATE');


  }

  amountOfMeals;

  //sliders
  eatenKcalValue = 50;
  percentOfProtein = 11;
  percentOfFat = 11;
  percentOfCarbo = 11;
  eatenProteinValue = 0;
  eatenFatValue = 0;
  eatenCarboValue = 0;

  chosenProduct: Product;
  meals: Map<number, Array<Product>>;

  iWantToSearch = false;
  searchedProductListBasedOnInputChange: Array<Product> = [];
  transformMetricMapMetricToWord  = new Map([['ML', 'milliliter'], ['GR', 'gram'], ['PC', 'pieces']]);
  initialData: DailyPayload = null ;
  ngOnInit(): void {
  }

  addProductForm = new FormGroup({

    searchForProduct : new FormControl(),
    amountOfProduct : new FormControl(),
  });
  addProductToThisMeal;


  reloadByDate(date)
  {
    this.meals = new Map<number, Array<Product>>();
    this.meals.set(1, new Array<Product>());
    this.meals.set(2, new Array<Product>());
    this.meals.set(3, new Array<Product>());
    this.meals.set(4, new Array<Product>());
    this.meals.set(5, new Array<Product>());
    this.dailyService.getDailyByDate(date.toString()).toPromise().then(response=>
    {
      response.productAmountMealNumbers.forEach(e => {

        switch(e.mealNumber)
        {
          case 1:
            this.meals.get(1).push(e.product);
            break;
          case 2:
            this.meals.get(2).push(e.product);
            break;
          case 3:
            this.meals.get(3).push(e.product);
            break;
          case 4:
            this.meals.get(4).push(e.product);
            break;
          case 5:
            this.meals.get(5).push(e.product);
            break;
        }

        if(e.product.metric != 'PC')
        {
          this.eatenKcalValue += e.product.kcal * e.product.amount / 100;
          this.eatenProteinValue += e.product.protein * e.product.amount / 100;
          this.eatenFatValue += e.product.fat * e.product.amount / 100;
          this.eatenCarboValue += e.product.carbo * e.product.amount / 100;
        }else{
          this.eatenKcalValue += e.product.kcal * e.product.amount;
          this.eatenProteinValue += e.product.protein * e.product.amount;
          this.eatenFatValue += e.product.fat * e.product.amount ;
          this.eatenCarboValue += e.product.carbo * e.product.amount;
        }
      });
      this.amountOfMeals = Array(response.amountOfMeals).fill(0).map((x,i)=>i+1);
      this.initialData = response;

      //this.currentDate= response.date;
      console.log(response)
    }).then(   e =>
      {
        console.log(e);
        this.percentOfProtein = (this.initialData.protein * 4) * 100 / this.initialData.kcal;
        this.percentOfFat = (this.initialData.fat * 9) * 100 / this.initialData.kcal;
        this.percentOfCarbo = (this.initialData.carbo * 4) * 100 / this.initialData.kcal;
      }
    ).catch(err => {
      console.log(err);
    })
  }

  remove(meal, product) {
    console.log(this.meals.get(meal).splice(product, 1));
    if(product.metric != 'PC')
    {
      this.eatenKcalValue -= product.kcal * product.amount / 100;
      this.eatenProteinValue -= product.protein * product.amount / 100;
      this.eatenFatValue -= product.fat * product.amount / 100;
      this.eatenCarboValue -= product.carbo * product.amount / 100;
    }else{
      this.eatenKcalValue -= product.kcal *product.amount;
      this.eatenProteinValue -= product.protein * product.amount;
      this.eatenFatValue -= product.fat * product.amount ;
      this.eatenCarboValue -= product.carbo * product.amount;
    }
  }

  decrementDay() {
    this.currentDate = formatDate(new Date(localStorage.getItem('CURRENT_DATE') ).getTime() - 1*24*60*60*1000, 'yyyy-MM-dd', 'en');
    localStorage.setItem('CURRENT_DATE', this.currentDate);
    this.reloadByDate(this.currentDate);
  }

  incrementDay() {
    this.currentDate = formatDate(new Date(localStorage.getItem('CURRENT_DATE') ).getTime() + 1*24*60*60*1000, 'yyyy-MM-dd', 'en');
    localStorage.setItem('CURRENT_DATE', this.currentDate);
    this.reloadByDate(this.currentDate);
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
    this.meals.get(this.addProductToThisMeal).push(this.chosenProduct);
    this.addProductForm.reset();
    this.searchedProductListBasedOnInputChange = new Array<Product>();
    if(this.chosenProduct.metric != 'PC')
    {
      this.eatenKcalValue += this.chosenProduct.kcal * this.chosenProduct.amount / 100;
      this.eatenProteinValue += this.chosenProduct.protein * this.chosenProduct.amount / 100;
      this.eatenFatValue += this.chosenProduct.fat * this.chosenProduct.amount / 100;
      this.eatenCarboValue += this.chosenProduct.carbo * this.chosenProduct.amount / 100;
    }else{
      this.eatenKcalValue += this.chosenProduct.kcal * this.chosenProduct.amount;
      this.eatenProteinValue += this.chosenProduct.protein * this.chosenProduct.amount;
      this.eatenFatValue += this.chosenProduct.fat * this.chosenProduct.amount ;
      this.eatenCarboValue += this.chosenProduct.carbo * this.chosenProduct.amount;
    }
  }

  save() {

      let dailyRequest = [];

      for (let [key, products] of this.meals)
      {
        for(let product of products)
        {
          let productAmountMealNumber = new ProductAmountMealNumber();
          productAmountMealNumber.mealNumber = key;
          productAmountMealNumber.product = product;
          dailyRequest.push(productAmountMealNumber);
        }
      }
      this.dailyService.updateDaily(dailyRequest, localStorage.getItem('CURRENT_DATE')).toPromise().then(data =>
      {

        console.log(data);
      }).catch(error =>
      {

        console.log(error)
      });

      console.log(dailyRequest)
  }
}
