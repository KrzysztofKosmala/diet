import { AuthService } from '../service/auth.service';
import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { Router } from "@angular/router";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {FormComponentBase} from "../signup/infrastructure/form-component-base";
import {map} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends FormComponentBase implements OnInit, AfterViewInit{
  invalidLogin: boolean;
  form!: FormGroup;
  // @ts-ignore
  @ViewChild('usernameOrEmail') firstItem: ElementRef;
  constructor(
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder) {
    super();
    this.validationMessages = {
      usernameOrEmail: {
        required: 'Username or email name is required.',
      },

      password: {
        required: 'Password is required.',
      },

    };
    this.formErrors = {
      usernameOrEmail: '',
      password: '',
    };}

  login(credentials) {
    this.authService.login(credentials)
      .subscribe(data =>
        {
          this.invalidLogin = false;
          localStorage.setItem('TOKEN', data.accessToken);
          localStorage.setItem('CURRENT_DATE', formatDate(new Date(), 'yyyy-MM-dd', 'en').toString());
          this.router.navigate(['/user/hello']);
        }
        ,(error) => {
          this.invalidLogin = true;
        }
      )

  }
  ngOnInit(): void {
    this.form = this.formBuilder.group({
      usernameOrEmail: ['', [
        Validators.required
      ]],
      password: ['', [
        Validators.required
      ]]
    })
  }


  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstItem.nativeElement.focus();
    }, 250);
    this.startControlMonitoring(this.form);
  }
}
