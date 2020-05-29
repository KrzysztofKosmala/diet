import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  currentUserName = this.authService.currentUser.name;

  constructor(
    private authService: AuthService) { }

  ngOnInit(): void {
  }

}
