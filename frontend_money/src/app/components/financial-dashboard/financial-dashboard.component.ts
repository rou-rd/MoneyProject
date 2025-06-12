import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

interface CategoryExpense {
  id: number;
  name: string;
  icon: string;
  color: string;
  amount: number;
  budget: number;
  percentage: number;
}

interface QuickTransaction {
  type: 'income' | 'expense';
  amount: number;
  category: string;
  description: string;
}

@Component({
  selector: 'app-financial-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './financial-dashboard.component.html',
  styleUrls: ['./financial-dashboard.component.css']
})
export class FinancialDashboardComponent implements OnInit {
  totalIncome = 5420.50;
  totalExpenses = 3280.75;
  currentMonth = 'June';
  
  categories: CategoryExpense[] = [];
  showTransactionModal = false;
  transactionForm: FormGroup;
  selectedTransactionType: 'income' | 'expense' = 'expense';

  constructor(private fb: FormBuilder) {
    this.transactionForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]],
      category: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categories = [
      {
        id: 1,
        name: 'Shopping',
        icon: '🛒',
        color: '#ff6b9d',
        amount: 450,
        budget: 500,
        percentage: 90
      },
      {
        id: 2,
        name: 'Food',
        icon: '🍽️',
        color: '#4ecdc4',
        amount: 320,
        budget: 400,
        percentage: 80
      },
      {
        id: 3,
        name: 'Transport',
        icon: '🚗',
        color: '#45b7d1',
        amount: 180,
        budget: 300,
        percentage: 60
      },
      {
        id: 4,
        name: 'Utilities',
        icon: '💡',
        color: '#f9ca24',
        amount: 150,
        budget: 200,
        percentage: 75
      },
      {
        id: 5,
        name: 'Entertainment',
        icon: '🎬',
        color: '#6c5ce7',
        amount: 120,
        budget: 200,
        percentage: 60
      },
      {
        id: 6,
        name: 'Health',
        icon: '🏥',
        color: '#a29bfe',
        amount: 80,
        budget: 150,
        percentage: 53
      },
      {
        id: 7,
        name: 'Education',
        icon: '📚',
        color: '#fd79a8',
        amount: 200,
        budget: 250,
        percentage: 80
      },
      {
        id: 8,
        name: 'Travel',
        icon: '✈️',
        color: '#00b894',
        amount: 300,
        budget: 400,
        percentage: 75
      },
      {
        id: 9,
        name: 'Gifts',
        icon: '🎁',
        color: '#e17055',
        amount: 100,
        budget: 150,
        percentage: 67
      },
      {
        id: 10,
        name: 'Personal',
        icon: '👤',
        color: '#74b9ff',
        amount: 90,
        budget: 120,
        percentage: 75
      },
      {
        id: 11,
        name: 'Subscriptions',
        icon: '📱',
        color: '#fd79a8',
        amount: 45,
        budget: 60,
        percentage: 75
      },
      {
        id: 12,
        name: 'Other',
        icon: '📝',
        color: '#636e72',
        amount: 65,
        budget: 100,
        percentage: 65
      }
    ];
  }

  get netBalance(): number {
    return this.totalIncome - this.totalExpenses;
  }

  get expensePercentage(): number {
    return this.totalIncome > 0 ? (this.totalExpenses / this.totalIncome) * 100 : 0;
  }

  openTransactionModal(type: 'income' | 'expense'): void {
    this.selectedTransactionType = type;
    this.transactionForm.reset();
    this.showTransactionModal = true;
  }

  closeTransactionModal(): void {
    this.showTransactionModal = false;
    this.transactionForm.reset();
  }

  addTransaction(): void {
    if (this.transactionForm.valid) {
      const transaction: QuickTransaction = {
        type: this.selectedTransactionType,
        amount: this.transactionForm.value.amount,
        category: this.transactionForm.value.category,
        description: this.transactionForm.value.description
      };

      // Update totals
      if (transaction.type === 'income') {
        this.totalIncome += transaction.amount;
      } else {
        this.totalExpenses += transaction.amount;
        
        // Update category spending
        const category = this.categories.find(c => c.name === transaction.category);
        if (category) {
          category.amount += transaction.amount;
          category.percentage = (category.amount / category.budget) * 100;
        }
      }

      this.closeTransactionModal();
    }
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  }

  getCategoryProgress(category: CategoryExpense): number {
    return Math.min(category.percentage, 100);
  }

  getCategoryStatus(category: CategoryExpense): 'safe' | 'warning' | 'danger' {
    if (category.percentage >= 90) return 'danger';
    if (category.percentage >= 75) return 'warning';
    return 'safe';
  }
}