import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoggedInUser } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly USER_KEY = 'currentUser';
  private readonly TOKEN_KEY = 'authToken';

  constructor(private router: Router) {}

  setUser(user: LoggedInUser): void {
    sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  setToken(token: string): void {
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  getUser(): LoggedInUser | null {
    const userStr = sessionStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  isLoggedIn(): boolean {
    return this.getUser() !== null && this.getToken() !== null;
  }

  isAdmin(): boolean {
    return this.getUser()?.role === 'ADMIN';
  }

  logout(): void {
    sessionStorage.removeItem(this.USER_KEY);
    sessionStorage.removeItem(this.TOKEN_KEY);
    this.router.navigate(['/admin/login']);
  }

  getUserName(): string {
    return this.getUser()?.name || 'User';
  }

  getUserId(): number | null {
    return this.getUser()?.id || null;
  }
}