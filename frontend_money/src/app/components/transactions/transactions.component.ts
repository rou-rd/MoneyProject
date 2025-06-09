import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ApiService, Transaction } from '../../services/api.service';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  searchTerm = '';
  selectedCategory = '';
  selectedStatus = '';
  selectedType = '';
  
  currentPage = 1;
  pageSize = 10;
  totalTransactions = 0;
  totalPages = 0;

  showModal = false;
  editingTransaction: Transaction | null = null;
  transactionForm: FormGroup;
  isSaving = false;

  constructor(
    private apiService: ApiService,
    private fb: FormBuilder
  ) {
    this.transactionForm = this.fb.group({
      description: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      type: ['debit', [Validators.required]],
      category: ['food', [Validators.required]],
      date: [new Date().toISOString().split('T')[0], [Validators.required]],
      status: ['completed', [Validators.required]]
    });

    // Mock data for demo
  }

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.apiService.getTransactions(this.currentPage - 1, this.pageSize).subscribe({
      next: (response) => {
        this.transactions = response.content || response;
        this.totalTransactions = response.totalElements || this.transactions.length;
        this.totalPages = Math.ceil(this.totalTransactions / this.pageSize);
        this.filterTransactions();
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
        // Use mock data when API is not available
        this.filterTransactions();
      }
    });
  }



Transactions(): void {
    const mockTransactions: Transaction[] = [
      {
        id: 1,
        date: new Date('2024-01-15'),
        description: 'Salary Payment',
        amount: 5000,
        type: 'credit',
        status: 'completed',
        category: 'salary'
      },
      {
        id: 2,
        date: new Date('2024-01-14'),
        description: 'Grocery Shopping',
        amount: 125.50,
        type: 'debit',
        status: 'completed',
        category: 'food'
      },
      {
        id: 3,
        date: new Date('2024-01-13'),
        description: 'Uber Ride',
        amount: 25.00,
        type: 'debit',
        status: 'completed',
        category: 'transport'
      },
      {
        id: 4,
        date: new Date('2024-01-12'),
        description: 'Freelance Project',
        amount: 1200,
        type: 'credit',
        status: 'pending',
        category: 'freelance'
      },
      {
        id: 5,
        date: new Date('2024-01-11'),
        description: 'Netflix Subscription',
        amount: 15.99,
        type: 'debit',
        status: 'completed',
        category: 'entertainment'
      },
      {
        id: 6,
        date: new Date('2024-01-10'),
        description: 'Electricity Bill',
        amount: 89.50,
        type: 'debit',
        status: 'completed',
        category: 'utilities'
      },
      {
        id: 7,
        date: new Date('2024-01-09'),
        description: 'Online Shopping',
        amount: 299.99,
        type: 'debit',
        status: 'failed',
        category: 'shopping'
      },
      {
        id: 8,
        date: new Date('2024-01-08'),
        description: 'Restaurant Dinner',
        amount: 85.00,
        type: 'debit',
        status: 'completed',
        category: 'food'
      }
    ];

    this.transactions = mockTransactions;
    this.totalTransactions = mockTransactions.length;
    this.totalPages = Math.ceil(this.totalTransactions / this.pageSize);
  }

  filterTransactions(): void {
    let filtered = [...this.transactions];

    if (this.searchTerm) {
      filtered = filtered.filter(t => 
        t.description.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        t.id.toString().includes(this.searchTerm)
      );
    }

    if (this.selectedCategory) {
      filtered = filtered.filter(t => t.category === this.selectedCategory);
    }

    if (this.selectedStatus) {
      filtered = filtered.filter(t => t.status === this.selectedStatus);
    }

    if (this.selectedType) {
      filtered = filtered.filter(t => t.type === this.selectedType);
    }

    this.filteredTransactions = filtered;
    this.totalTransactions = filtered.length;
    this.totalPages = Math.ceil(this.totalTransactions / this.pageSize);
    
    // Reset to first page if current page is out of bounds
    if (this.currentPage > this.totalPages) {
      this.currentPage = 1;
    }
  }

  openTransactionModal(): void {
    this.editingTransaction = null;
    this.transactionForm.reset({
      type: 'debit',
      category: 'food',
      date: new Date().toISOString().split('T')[0],
      status: 'completed'
    });
    this.showModal = true;
  }

  editTransaction(transaction: Transaction): void {
    this.editingTransaction = transaction;
    this.transactionForm.patchValue({
      description: transaction.description,
      amount: transaction.amount,
      type: transaction.type,
      category: transaction.category,
      date: new Date(transaction.date).toISOString().split('T')[0],
      status: transaction.status
    });
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingTransaction = null;
    this.transactionForm.reset();
  }

  saveTransaction(): void {
    if (this.transactionForm.valid) {
      this.isSaving = true;
      const formData = this.transactionForm.value;
      
      const transactionData = {
        ...formData,
        date: new Date(formData.date)
      };

      if (this.editingTransaction) {
        this.apiService.updateTransaction(this.editingTransaction.id, transactionData).subscribe({
          next: (response) => {
            this.isSaving = false;
            this.closeModal();
            this.loadTransactions();
          },
          error: (error) => {
            this.isSaving = false;
            console.error('Error updating transaction:', error);
            // For demo purposes, simulate success
            setTimeout(() => {
              this.isSaving = false;
              this.closeModal();
              // Update the transaction in the local array
              const index = this.transactions.findIndex(t => t.id === this.editingTransaction!.id);
              if (index !== -1) {
                this.transactions[index] = { ...this.editingTransaction!, ...transactionData };
                this.filterTransactions();
              }
            }, 1000);
          }
        });
      } else {
        this.apiService.createTransaction(transactionData).subscribe({
          next: (response) => {
            this.isSaving = false;
            this.closeModal();
            this.loadTransactions();
          },
          error: (error) => {
            this.isSaving = false;
            console.error('Error creating transaction:', error);
            // For demo purposes, simulate success
            setTimeout(() => {
              this.isSaving = false;
              this.closeModal();
              // Add the transaction to the local array
              const newTransaction: Transaction = {
                id: Math.max(...this.transactions.map(t => t.id)) + 1,
                ...transactionData
              };
              this.transactions.unshift(newTransaction);
              this.filterTransactions();
            }, 1000);
          }
        });
      }
    }
  }

  deleteTransaction(id: number): void {
    if (confirm('Are you sure you want to delete this transaction?')) {
      this.apiService.deleteTransaction(id).subscribe({
        next: () => {
          this.loadTransactions();
        },
        error: (error) => {
          console.error('Error deleting transaction:', error);
          // For demo purposes, simulate success
          this.transactions = this.transactions.filter(t => t.id !== id);
          this.filterTransactions();
        }
      });
    }
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  }

  getCategoryLabel(category: string): string {
    const labels: { [key: string]: string } = {
      food: 'Food & Dining',
      transport: 'Transportation',
      shopping: 'Shopping',
      utilities: 'Utilities',
      entertainment: 'Entertainment',
      salary: 'Salary',
      freelance: 'Freelance'
    };
    return labels[category] || category;
  }

  getStatusLabel(status: string): string {
    return status.charAt(0).toUpperCase() + status.slice(1);
  }

  // Pagination methods
  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadTransactions();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadTransactions();
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadTransactions();
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPages = 5;
    let start = Math.max(1, this.currentPage - Math.floor(maxPages / 2));
    let end = Math.min(this.totalPages, start + maxPages - 1);
    
    if (end - start < maxPages - 1) {
      start = Math.max(1, end - maxPages + 1);
    }
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  get Math() {
    return Math;
  }
}