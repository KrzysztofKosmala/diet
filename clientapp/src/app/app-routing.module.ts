import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {SignupComponent} from "./signup/signup.component";
import {UserComponent} from "./user/user.component";
import {UserDetailInfoComponent} from "./user-detail-info/user-detail-info.component";
import {ProductComponent} from "./product/product.component";
import {RecipeComponent} from "./recipe/recipe.component";
import {DailyComponent} from "./daily/daily.component";
import {HelloComponent} from "./hello/hello.component";
import {ShoppingListComponent} from "./shopping-list/shopping-list.component";


const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  {
    path: 'user',
    component: UserComponent,
    children:
      [
        {path: 'details' ,component: UserDetailInfoComponent},
        {path: 'product' ,component: ProductComponent},
        {path: 'recipe' ,component: RecipeComponent},
        {path: 'daily' ,component: DailyComponent},
        {path: 'hello' ,component: HelloComponent},
        {path: 'shoppingList' ,component: ShoppingListComponent}

      ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
