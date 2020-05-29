import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import { CrossFieldErrorMatcher } from '../signup/infrastructure/cross-field-error-matcher';
import { passwordsDoNotMatch } from '../signup/infrastructure/passwords-do-not-match.validator';
import { FormComponentBase } from '../signup/infrastructure/form-component-base';
import {HttpErrorResponse} from "@angular/common/http";

/*TODO
*  email to comfirm user email
*/

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent extends FormComponentBase implements OnInit, AfterViewInit {
  // @ts-ignore
  @ViewChild('email') firstItem: ElementRef;

  form!: FormGroup;
  hidePassword: boolean = true;
  errorMatcher = new CrossFieldErrorMatcher();

  invalidTry: boolean;
  invalidMessage;

  constructor(
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder) {
    super();

    this.validationMessages = {
      username: {
        required: 'User name is required.',
        minlength: 'User name minimum length is 3.',
        maxlength: 'User name maximum length is 40.',
        pattern: 'User name minimum length 3, allowed characters letters, numbers only. No spaces.'
      },
      name: {
        required: 'User name is required.',
        minlength: 'User name minimum length is 3.',
        maxlength: 'User name maximum length is 40.',
        pattern: 'User name minimum length 3, allowed characters letters, numbers only.'
      },
      password: {
        required: 'Password is required.',
        minlength: 'Password minimum length is 6.',
        maxlength: 'Password maximum length is 15.',
        pattern: 'Minimum length 6, requires one letter, one number, one special character, no spaces.'
      },
      confirmPassword: {
        required: 'Confirm password is required.',
        minlength: 'Confirm password minimum length is 6.',
        maxlength: 'Confirm password maximum length is 15.',
        pattern: 'Minimum length 6, requires one letter, one number, one special character, no spaces.',
        passwordsDoNotMatch: 'Passwords must match.'
      },
      email: {
        required: 'Email is required.',
        email: 'Email is not properly formatted.',
      },
      passwordsGroup: {
        passwordsDoNotMatch: 'Passwords must match.'
      }
  };
    this.formErrors = {
      username: '',
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      passwordsGroup: ''
    };
  }

  signup(credentials) {
    var json =
      {
        name: credentials.name,
        username: credentials.username,
        email: credentials.email,
        password: credentials.passwordsGroup.password
      };
    this.authService.signup(json)
      .subscribe(data =>
        {
          this.router.navigate(['/login']);
        }
        ,(error) => {
          var errorJson = error.json();
          this.invalidMessage = errorJson.message;
          this.invalidTry = true;
        }
      )

  }

  ngOnInit(): void
  {
    this.form = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(40),
        Validators.pattern('^[a-zA-Z0-9]*$')]],
      name: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(40),
        Validators.pattern(".*\\S.*[a-zA-z0-9 ]")]],
      email: ['', [
        Validators.required,
        Validators.email]],
      passwordsGroup: this.formBuilder.group({
        password: ['', [
          Validators.required,
          Validators.maxLength(15),
          Validators.pattern('^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,}$')]],
        confirmPassword: ['', [
          Validators.required,
          Validators.maxLength(15),
          Validators.pattern('^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,}$')]],
      }, { validators: passwordsDoNotMatch })
    });
  }
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstItem.nativeElement.focus();
    }, 250);
    this.startControlMonitoring(this.form);
  }
  registerClicked(): void {
    if (this.form.invalid) {
      return;
    }
    alert('Registration Complete');
  }
}
