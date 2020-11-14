import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserDetailInfoServiceService} from "../service/user-detail-info-service.service";
import {ProductService} from "../service/product.service";
import {Product} from "../service/payload/Product";
import {FormControl, FormGroup} from "@angular/forms";
import {RecipeService} from "../service/recipe.service";

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css']
})
export class RecipeComponent implements OnInit {

  constructor(private router: Router,
  private productService: ProductService,
              private recipeService: RecipeService) { }

  successfullyAdded;
  isMnValueOk = true;
  isProductInDb = true;
  chossenProductList: Array<Product> = [];
  searchedProductListBasedOnInputChange: Array<Product> = [];

  selectedAsIngredient: Product;

  addProductForm = new FormGroup({

    searchForProduct : new FormControl(),
    amountOfProduct : new FormControl(),
  });

  recipeForm = new FormGroup({

    name : new FormControl(),
    description: new FormControl(),
    type1: new FormControl(),
    type2: new FormControl(),
  });

  transformMetricMapWordToMetric  = new Map([['milliliter', 'ML'], ['gram', 'GR'], ['piece', 'PC']]);
  transformMetricMapMetricToWord  = new Map([['ML', 'milliliter'], ['GR', 'gram'], ['PC', 'pieces']]);

  addProductToIngredients(form)
  {
            if(form.amountOfProduct >= this.selectedAsIngredient.min_value) {
              this.selectedAsIngredient.amount = form.amountOfProduct;
              this.chossenProductList.push(this.selectedAsIngredient);
              this.isMnValueOk =true;
              this.isProductInDb = true;
              this.addProductForm.reset();
            }else {
              this.isMnValueOk = false;
            }
  }

  ngOnInit(): void {
  }

  createRecipe(formValue) {

    //zamienic na Recipe

    var json =
      {
        name: formValue.name,
        description: formValue.description,
        type1 : formValue.type1,
        type2 : formValue.type2,
        products: this.chossenProductList,
      };

    this.recipeService.createRecipe(json).toPromise()
      .then(data =>
      {
        console.log(data);
        this.successfullyAdded =true;
        this.recipeForm.reset();
        this.chossenProductList= [];
        this.searchedProductListBasedOnInputChange =[];
      }
      )
      .catch(error =>
      {
        console.log(error);
        this.successfullyAdded = false;
      }
    )


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

  onSelection(e, v) {
    this.selectedAsIngredient = null;

    this.selectedAsIngredient = e.option.value;
    console.log("selected as ogridient")
    console.log(e.option.value)

  }
}
