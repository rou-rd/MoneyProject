import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

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

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api'; // Spring Boot API URL

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  // Dashboard API calls
  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`, {
      headers: this.getHeaders()
    });
  }

  getChartData(type: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard/chart/${type}`, {
      headers: this.getHeaders()
    });
  }

  // Transaction API calls
  getTransactions(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}/transactions?page=${page}&size=${size}`, {
      headers: this.getHeaders()
    });
  }

  createTransaction(transaction: Partial<Transaction>): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/transactions`, transaction, {
      headers: this.getHeaders()
    });
  }

  updateTransaction(id: number, transaction: Partial<Transaction>): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.apiUrl}/transactions/${id}`, transaction, {
      headers: this.getHeaders()
    });
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/transactions/${id}`, {
      headers: this.getHeaders()
    });
  }

  // Profile API calls
  updateProfile(profileData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile`, profileData, {
      headers: this.getHeaders()
    });
  }

  changePassword(passwordData: { oldPassword: string; newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/profile/change-password`, passwordData, {
      headers: this.getHeaders()
    });
  }
}