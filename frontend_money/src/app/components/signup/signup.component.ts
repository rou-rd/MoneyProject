import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

export interface Role {
  userId: number;
  roleName: string;
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  roles: Role[] = [];
  isLoading = false;
  signupError = '';
  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phoneNumber: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
      roleid: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  loading = false;
  error  = '';
  
  loadRoles(): void {
    this.loading = true;
    this.apiService.getRoles().subscribe({
      next: (roles) => {
         console.log('Roles loaded from API:', roles);
        this.roles = roles;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading roles:', error);
        // Mock roles for demo
        this.roles = [
          { userId: 1, roleName: 'Admin' },
          { userId: 2, roleName: 'User' },
          { userId: 3, roleName: 'Manager' },
          { userId: 4, roleName: 'Customer' }
        ];
      }
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    
    return null;
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }
signupSuccess: string = '';
  onSubmit(): void {
    if (this.signupForm.valid) {
      this.isLoading = true;
      this.signupError = '';
      this.signupSuccess = '';

      const signupData = {
        username: this.signupForm.value.username,
        email: this.signupForm.value.email,
        password: this.signupForm.value.password,
        firstName: this.signupForm.value.firstName,
        lastName: this.signupForm.value.lastName,
        phoneNumber: this.signupForm.value.phoneNumber,
        birthDate: this.signupForm.value.birthDate,
        roleid: parseInt(this.signupForm.value.roleid)
      };

     this.apiService.registerUser(signupData).subscribe({
  next: (response) => {
    console.log('Response from server:', response); // ici response est une string
    this.isLoading = false;
    this.signupSuccess = 'Account created successfully! Please login.';
      setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
  },
  error: (error) => {
    this.isLoading = false;
    this.signupError = error.error?.message || 'Registration failed. Please try again.';
  }
});
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}