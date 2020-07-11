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
  proList: Array<Product> = [];


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

      this.productService.getProductByName(form.searchForProduct).toPromise()
        .then(response =>{
          if(response.metric === this.transformMetricMap.get(form.metric))
          {
            if(form.amountOfProduct > response.min_value) {
              response.amount = form.amountOfProduct;
              response.metric = form.metric;
              this.proList.push(response);
              this.isMetricOk = true;
              this.isMnValueOk =true;
              this.isProductInDb = true;
              console.log(this.proList);
            }else {
              this.isMnValueOk = false;
            }
          }
          else{
            this.isMetricOk = false;
          }

          }
        ).catch(error =>
      {
        this.isProductInDb = false;
         console.log(error);
      })
  }

  ngOnInit(): void {
  }

  createRecipe(formValue) {

    this.proList.forEach(product => {
      product.metric = this.transformMetricMap.get(product.metric)
    });

    var json =
      {
        name: formValue.name,
        description: formValue.description,
        type: formValue.type,
        products: this.proList,
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
}
