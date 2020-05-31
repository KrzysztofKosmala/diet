import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import {Router} from "@angular/router";


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  currentUserName = this.authService.currentUser.name;

  clicked = false;

  constructor(
    private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
  }

  isClicked()
  {
      this.clicked = !this.clicked;
  }
  logout()
  {
    this.authService.logout();
    this.router.navigate(['/login'])
  }
}
