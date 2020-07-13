import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserDetailInfoServiceService} from "../service/user-detail-info-service.service";
import {ProductService} from "../service/product.service";
import {Product} from "../payload/Product";
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

  isMetricOk = true;
  isMnValueOk = true;
  isProductInDb = true;
  chossenProductList: Array<Product> = [];
  searchedProductListBasedOnInputChange: Array<Product> = [];

  selectedAsIngredient: Product;

  addProductForm = new FormGroup({

    searchForProduct : new FormControl(),
    amountOfProduct : new FormControl(),
    metric : new FormControl(),
  });

  recipeForm = new FormGroup({

    name : new FormControl(),
    description: new FormControl(),
    type: new FormControl(),
  });

  transformMetricMap  = new Map([['milliliter', 'ML'], ['gram', 'GR'], ['piece', 'PC']]);

  searchProduct(form)
  {


          if(this.selectedAsIngredient.metric === this.transformMetricMap.get(form.metric))
          {
            if(form.amountOfProduct > this.selectedAsIngredient.min_value) {
              this.selectedAsIngredient.amount = form.amountOfProduct;
              this.selectedAsIngredient.metric = form.metric;
              this.chossenProductList.push(this.selectedAsIngredient);
              this.isMetricOk = true;
              this.isMnValueOk =true;
              this.isProductInDb = true;
              console.log(this.chossenProductList);
            }else {
              this.isMnValueOk = false;
            }
          }
          else{
            this.isMetricOk = false;
          }



  }

  ngOnInit(): void {
  }

  createRecipe(formValue) {

    this.chossenProductList.forEach(product => {
      product.metric = this.transformMetricMap.get(product.metric)
    });

    var json =
      {
        name: formValue.name,
        description: formValue.description,
        type: formValue.type,
        products: this.chossenProductList,
      };

    this.recipeService.createRecipe(json).toPromise()
      .then(data =>

        console.log(data)

      )
      .catch(error =>
      {
        console.log(error)
      }
    )

  }

  pingingSearch(eventKey) {
    console.log(eventKey);
    this.productService.getProductByPartOfName(eventKey).toPromise().then(response=>
    {
      this.searchedProductListBasedOnInputChange = [];
      console.log(response);
      response.forEach(element => this.searchedProductListBasedOnInputChange.push(element))
      ;
      console.log(this.searchedProductListBasedOnInputChange)
    }

    ).catch(err => console.log(err))
  }

  onSelection(e, v) {
    this.selectedAsIngredient = null;

    this.selectedAsIngredient = e.option.value;
    console.log(e.option.value)
  }
}
