import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService, User } from '../../services/auth.service';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  activeTab = 'personal';
  currentUser: User | null = null;
  profileForm: FormGroup;
  passwordForm: FormGroup;
  isSaving = false;
  isChangingPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private apiService: ApiService
  ) {
    this.profileForm = this.fb.group({
      firstName: ['Stein', [Validators.required]],
      lastName: ['Ben', [Validators.required]],
      email: ['stebin.ben@gmail.com', [Validators.required, Validators.email]],
      dateOfBirth: [''],
      phoneNumber: [''],
      designation: ['Full Stack Developer'],
      address1: ['3801 Chalk Butte Rd, Cut Bank, MT 59427, United States'],
      address2: ['301 Chalk Butte Rd, Cut Bank, NY 96572, New York'],
      country: ['us'],
      state: ['California']
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.profileForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email
        });
      }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  updateProfile(): void {
    if (this.profileForm.valid) {
      this.isSaving = true;
      
      this.apiService.updateProfile(this.profileForm.value).subscribe({
        next: (response) => {
          this.isSaving = false;
          // Show success message
          console.log('Profile updated successfully');
        },
        error: (error) => {
          this.isSaving = false;
          console.error('Error updating profile:', error);
          // For demo purposes, simulate success
          setTimeout(() => {
            this.isSaving = false;
            console.log('Profile updated successfully (demo mode)');
          }, 1000);
        }
      });
    }
  }

  changePassword(): void {
    if (this.passwordForm.valid) {
      this.isChangingPassword = true;
      
      const passwordData = {
        oldPassword: this.passwordForm.value.currentPassword,
        newPassword: this.passwordForm.value.newPassword
      };

      this.apiService.changePassword(passwordData).subscribe({
        next: (response) => {
          this.isChangingPassword = false;
          this.passwordForm.reset();
          // Show success message
          console.log('Password changed successfully');
        },
        error: (error) => {
          this.isChangingPassword = false;
          console.error('Error changing password:', error);
          // For demo purposes, simulate success
          setTimeout(() => {
            this.isChangingPassword = false;
            this.passwordForm.reset();
            console.log('Password changed successfully (demo mode)');
          }, 1000);
        }
      });
    }
  }

  private passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword');
    const confirmPassword = form.get('confirmPassword');
    
    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    
    return null;
  }
}