import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  activeTab = 'admin';
  showPassword = false;
  isLoading = false;
  loginError = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['salma', [Validators.required]],
      password: ['password', [Validators.required]],
      rememberMe: [false]
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    // Update form values based on tab
    if (tab === 'admin') {
      this.loginForm.patchValue({
        username: 'salma',
        password: 'password'
      });
    } else {
      this.loginForm.patchValue({
       username: 'salma',
        password: 'password'
      });
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.loginError = '';

      const credentials = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
      };

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.isLoading = false;
          this.loginError = error.error?.message || 'Login failed. Please try again.';
          
          // For demo purposes, allow login with any credentials
          if (error.status === 0 || error.status === 404) {
            // Mock successful login when API is not available
            const mockResponse = {
              token: 'mock-jwt-token',
              user: {
                username: credentials.username,
              }
            };
            
            localStorage.setItem('token', mockResponse.token);
            localStorage.setItem('user', JSON.stringify(mockResponse.user));
            this.router.navigate(['/dashboard']);
          }
        }
      });
    }
  }
}