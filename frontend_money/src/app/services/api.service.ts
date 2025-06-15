import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { NotificationService } from './notification.service';
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export interface DashboardStats {
  totalPageViews: number;
  totalUsers: number;
  totalOrders: number;
  totalSales: number;
  pageViewsGrowth: number;
  usersGrowth: number;
  ordersGrowth: number;
  salesGrowth: number;
}

export interface Transaction {
  id: number;
  date: Date;
  description: string;
  amount: number;
  type: 'credit' | 'debit';
  status: 'completed' | 'pending' | 'failed';
  category: string;
}

export interface RegisterUserRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  roleid: number;
}

export interface CreateAccountRequest {
  currency: string;
  balance: number;
  userId: number;
}

export interface Role {
  userId: number;
  roleName: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8081'; // Spring Boot API URL

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private notificationService: NotificationService
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  registerUser(userData: RegisterUserRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/users/register`, userData, {
      responseType: 'text'
    }).pipe(
      tap(response => {
        this.notificationService.handleApiResponse(response, 'Registration Successful');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Registration Failed');
        return throwError(() => error);
      })
    );
  }

  // Account Creation
  createBankAccount(accountData: CreateAccountRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/Account`, accountData, {
      headers: this.getHeaders()
    }).pipe(
      tap(response => {
        this.notificationService.handleApiResponse(response, 'Account Created');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Account Creation Failed');
        return throwError(() => error);
      })
    );
  }

  // Dashboard API calls
  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        this.notificationService.handleApiError(error, 'Failed to Load Dashboard Stats');
        return throwError(() => error);
      })
    );
  }

  getChartData(type: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard/chart/${type}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        this.notificationService.handleApiError(error, 'Failed to Load Chart Data');
        return throwError(() => error);
      })
    );
  }

  // Transaction API calls
  getTransactions(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}/transactions?page=${page}&size=${size}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        this.notificationService.handleApiError(error, 'Failed to Load Transactions');
        return throwError(() => error);
      })
    );
  }

  createTransaction(transaction: Partial<Transaction>): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/transactions`, transaction, {
      headers: this.getHeaders()
    }).pipe(
      tap(response => {
        this.notificationService.showSuccess('Transaction Created', 'Transaction has been created successfully');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Transaction Creation Failed');
        return throwError(() => error);
      })
    );
  }

  updateTransaction(id: number, transaction: Partial<Transaction>): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.apiUrl}/transactions/${id}`, transaction, {
      headers: this.getHeaders()
    }).pipe(
      tap(response => {
        this.notificationService.showSuccess('Transaction Updated', 'Transaction has been updated successfully');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Transaction Update Failed');
        return throwError(() => error);
      })
    );
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/transactions/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      tap(() => {
        this.notificationService.showSuccess('Transaction Deleted', 'Transaction has been deleted successfully');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Transaction Deletion Failed');
        return throwError(() => error);
      })
    );
  }

  // Profile API calls
  updateProfile(profileData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile`, profileData, {
      headers: this.getHeaders()
    }).pipe(
      tap(response => {
        this.notificationService.showSuccess('Profile Updated', 'Your profile has been updated successfully');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Profile Update Failed');
        return throwError(() => error);
      })
    );
  }

  changePassword(passwordData: { oldPassword: string; newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/profile/change-password`, passwordData, {
      headers: this.getHeaders()
    }).pipe(
      tap(response => {
        this.notificationService.showSuccess('Password Changed', 'Your password has been changed successfully');
      }),
      catchError(error => {
        this.notificationService.handleApiError(error, 'Password Change Failed');
        return throwError(() => error);
      })
    );
  }

  getRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiUrl}/roles/getall`).pipe(
      catchError(error => {
        this.notificationService.handleApiError(error, 'Failed to Load Roles');
        return throwError(() => error);
      })
    );
  }
}