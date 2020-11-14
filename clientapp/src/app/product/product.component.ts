import { Component, OnInit } from '@angular/core';
import {Route, Router} from "@angular/router";
import {ProductService} from "../service/product.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-product',
  templateUrl: '../service/product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  createdSuccessfully: boolean = false;

  constructor(private router: Router, private productService: ProductService) { }

  ngOnInit(): void {
  }

  form = new FormGroup({

    divisible : new FormControl(),
    name : new FormControl(),
    metric : new FormControl(),
    min_value : new FormControl(),
/*    kcal : new FormControl(),*/
    protein : new FormControl(),
    fat : new FormControl(),
    carbo : new FormControl(),

  });

  metrics = ['GR',
  'ML',
  'PC']

  createProduct(product)
  {

  this.productService.createProduct(product).toPromise().then(data =>
  {
    this.createdSuccessfully = true;
    console.log(data);
  }).catch(error =>
  {

    console.log(error)
  })
  }

}
