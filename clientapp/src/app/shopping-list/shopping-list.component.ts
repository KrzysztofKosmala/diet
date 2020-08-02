import { Component, OnInit } from '@angular/core';
import {ShoppingListService} from "../service/shopping-list.service";
import {Product} from "../service/payload/Product";
import {FormControl, FormGroup} from "@angular/forms";
import {ProductService} from "../service/product.service";
import {ProductsWrapper} from "../service/payload/ProductsWrapper";

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.css']
})
export class ShoppingListComponent implements OnInit {
  private isMnValueOk: boolean;
  private isProductInDb: boolean;

  constructor(private shoppingListService: ShoppingListService, private productService: ProductService) { }

  products = new Array<Product>();
  searchedProductListBasedOnInputChange: Array<Product> = [];
  transformMetricMapMetricToWord  = new Map([['ML', 'milliliter'], ['GR', 'gram'], ['PC', 'pieces']]);
  selectedToAdd: Product;

  ngOnInit(): void {
    this.products= [];
    this.shoppingListService.getProducts().toPromise()
      .then(response => {
        console.log(response)

        this.products = response.products;
        console.log(this.products)
      })
  }
  addProductForm = new FormGroup({

    searchForProduct : new FormControl(),
    amountOfProduct : new FormControl(),
  });

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

  removeProduct(x) {
    this.products.splice(x, 1);
  }

  addProductToShoppingList(value) {

    if(value.amountOfProduct > this.selectedToAdd.min_value)
    {
      let isProductOnlist =false;
      console.log(this.products);
      let product: Product ;
      let amount : number;
        this.products.forEach(search =>{
        if(search.name===this.selectedToAdd.name)
        {
          product= search;

          console.log("product");
          console.log(product);
          console.log("amount");
          console.log(amount)
        }
      } );

      if(product === undefined)
      {
        this.selectedToAdd.amount = value.amountOfProduct;
        this.products.push(this.selectedToAdd);
      }else{
      ;

        product.amount = + value.amountOfProduct + product.amount;
      }


      this.isMnValueOk =true;
      this.isProductInDb = true;
      this.addProductForm.reset();
      this.searchedProductListBasedOnInputChange = [];
    }else {
      this.isMnValueOk = false;
    }
    console.log(this.selectedToAdd)
  }

  onSelection(e, v) {
    this.selectedToAdd = null;

    this.selectedToAdd = e.option.value;

    console.log(e.option.value)

  }

  save() {

    let productWrapper = new ProductsWrapper();
    productWrapper.products = this.products;
      this.shoppingListService.updateList(productWrapper).toPromise()
        .then( response => console.log(response))
        .catch(err => console.log(err))
  }
}
