import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

export interface Account {
  id: number;
  name: string;
  balance: number;
  currency: string;
  type: 'checking' | 'savings' | 'credit' | 'investment';
  status: 'active' | 'inactive';
  createdDate: Date;
}

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  accounts: Account[] = [];
  showModal = false;
  editingAccount: Account | null = null;
  accountForm: FormGroup;
  isSaving = false;

  constructor(
    private apiService: ApiService,
    private fb: FormBuilder
  ) {
    this.accountForm = this.fb.group({
      name: ['', [Validators.required]],
      balance: [0, [Validators.required, Validators.min(0)]],
      currency: ['USD', [Validators.required]],
      type: ['checking', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    // Mock data for demo
    this.accounts = [
      {
        id: 1,
        name: 'Main Checking',
        balance: 5420.50,
        currency: 'USD',
        type: 'checking',
        status: 'active',
        createdDate: new Date('2024-01-01')
      },
      {
        id: 2,
        name: 'Savings Account',
        balance: 12350.00,
        currency: 'USD',
        type: 'savings',
        status: 'active',
        createdDate: new Date('2024-01-15')
      },
      {
        id: 3,
        name: 'Credit Card',
        balance: -1250.75,
        currency: 'USD',
        type: 'credit',
        status: 'active',
        createdDate: new Date('2024-02-01')
      }
    ];
  }

  openAccountModal(): void {
    this.editingAccount = null;
    this.accountForm.reset({
      currency: 'USD',
      type: 'checking',
      balance: 0
    });
    this.showModal = true;
  }

  editAccount(account: Account): void {
    this.editingAccount = account;
    this.accountForm.patchValue({
      name: account.name,
      balance: account.balance,
      currency: account.currency,
      type: account.type
    });
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingAccount = null;
    this.accountForm.reset();
  }

  saveAccount(): void {
    if (this.accountForm.valid) {
      this.isSaving = true;
      const formData = this.accountForm.value;
      
      setTimeout(() => {
        if (this.editingAccount) {
          const index = this.accounts.findIndex(a => a.id === this.editingAccount!.id);
          if (index !== -1) {
            this.accounts[index] = { ...this.editingAccount, ...formData };
          }
        } else {
          const newAccount: Account = {
            id: Math.max(...this.accounts.map(a => a.id)) + 1,
            ...formData,
            status: 'active',
            createdDate: new Date()
          };
          this.accounts.push(newAccount);
        }
        this.isSaving = false;
        this.closeModal();
      }, 1000);
    }
  }

  deleteAccount(id: number): void {
    if (confirm('Are you sure you want to delete this account?')) {
      this.accounts = this.accounts.filter(a => a.id !== id);
    }
  }

  getAccountTypeIcon(type: string): string {
    const icons = {
      checking: 'M4 4a2 2 0 00-2 2v4a2 2 0 002 2V6h10a2 2 0 00-2-2H4z',
      savings: 'M3 6a3 3 0 013-3h10a1 1 0 01.8 1.6L14.25 8l2.55 3.4A1 1 0 0116 13H6a3 3 0 01-3-3V6z',
      credit: 'M4 4a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2H4zm0 2h12v2H4V6z',
      investment: 'M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3z'
    };
    return icons[type as keyof typeof icons] || icons.checking;
  }

  getTotalBalance(): number {
    return this.accounts.reduce((total, account) => total + account.balance, 0);
  }

  formatCurrency(amount: number, currency: string = 'USD'): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency
    }).format(amount);
  }
}