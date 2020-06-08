import { BrowserModule } from '@angular/platform-browser';

import { NgModule } from '@angular/core';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule, Http, BaseRequestOptions } from '@angular/http';
import { RouterModule } from '@angular/router';
import {AuthService} from "./service/auth.service";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import { WavesModule, ButtonsModule, IconsModule } from 'angular-bootstrap-md'
import {InterceptorService} from "./service/interceptor.service";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {HttpClientModule} from "@angular/common/http";
import { UserComponent } from './user/user.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import { UserDetailInfoComponent } from './user-detail-info/user-detail-info.component';
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";
import {UserDetailInfoServiceService} from "./service/user-detail-info-service.service";
import {MatSelectModule} from '@angular/material/select';



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    HeaderComponent,
    FooterComponent,
    UserComponent,
    UserDetailInfoComponent,



  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpModule,
    MDBBootstrapModule.forRoot(),
    RouterModule.forRoot([]),

    BrowserAnimationsModule,
    MatCheckboxModule,
    FlexLayoutModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    WavesModule,
    ButtonsModule,
    IconsModule,
    ReactiveFormsModule,
    MatSidenavModule,
    MatListModule,
    MatMenuModule,
    HttpClientModule,
    MatSelectModule
  ],
  providers: [AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true
    },
    UserDetailInfoServiceService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }