import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { NotificationService } from './notification.service';

export interface User {
  username: string;
  token: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/users';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService
  ) {
    this.loadUserFromStorage();
  }

  login(credentials: LoginRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { responseType: 'text' })
      .pipe(
        map((token: string) => {
          const user: User = { username: credentials.username, token };
          this.setAuthData(user);
          this.notificationService.showSuccess('Login Successful', `Welcome back, ${credentials.username}!`);
          return token;
        }),
        catchError(error => {
          this.notificationService.handleApiError(error, 'Login Failed');
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.notificationService.showInfo('Logged Out', 'You have been successfully logged out');
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token && !this.isTokenExpired(token);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private setAuthData(user: User): void {
    localStorage.setItem('token', user.token);
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private loadUserFromStorage(): void {
    const userStr = localStorage.getItem('user');
    if (userStr && this.isAuthenticated()) {
      const user = JSON.parse(userStr);
      this.currentUserSubject.next(user);
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp < Date.now() / 1000;
    } catch {
      return true;
    }
  }
}