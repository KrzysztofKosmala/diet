import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserDetailInfoServiceService} from "../service/user-detail-info-service.service";
import {ProductService} from "../service/product.service";
import {Product} from "../payload/Product";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css']
})
export class RecipeComponent implements OnInit {

  constructor(private router: Router,
  private productService: ProductService) { }

  proList: Array<Product> = [];


  form = new FormGroup({

    searchForProduct : new FormControl(),
  });

  searchProduct(name)
  {
    console.log(name.searchForProduct);
      this.productService.getProductByName(name.searchForProduct).toPromise()
        .then(response =>{
          this.proList.push(response);
          console.log(this.proList);

            
          }
        ).then(error =>
      {
         console.log(error);
      })
  }

  ngOnInit(): void {
  }

}
