import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

export interface Category {
  id: number;
  name: string;
  description: string;
  icon: string;
  color: string;
  type: 'income' | 'expense';
  budget?: number;
  spent?: number;
}

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  showModal = false;
  editingCategory: Category | null = null;
  categoryForm: FormGroup;
  isSaving = false;
  selectedType: 'all' | 'income' | 'expense' = 'all';

  categoryIcons = [
    { name: 'shopping', icon: '🛒' },
    { name: 'food', icon: '🍽️' },
    { name: 'transport', icon: '🚗' },
    { name: 'utilities', icon: '💡' },
    { name: 'entertainment', icon: '🎬' },
    { name: 'health', icon: '🏥' },
    { name: 'education', icon: '📚' },
    { name: 'salary', icon: '💰' },
    { name: 'freelance', icon: '💻' },
    { name: 'investment', icon: '📈' },
    { name: 'gift', icon: '🎁' },
    { name: 'other', icon: '📝' }
  ];

  categoryColors = [
    '#3b82f6', '#10b981', '#f59e0b', '#ef4444',
    '#8b5cf6', '#06b6d4', '#84cc16', '#f97316',
    '#ec4899', '#6366f1', '#14b8a6', '#a855f7'
  ];

  constructor(private fb: FormBuilder) {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required]],
      description: [''],
      icon: ['🛒', [Validators.required]],
      color: ['#3b82f6', [Validators.required]],
      type: ['expense', [Validators.required]],
      budget: [0, [Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    // Mock data for demo
    this.categories = [
      {
        id: 1,
        name: 'Food & Dining',
        description: 'Restaurants, groceries, and food delivery',
        icon: '🍽️',
        color: '#10b981',
        type: 'expense',
        budget: 500,
        spent: 320
      },
      {
        id: 2,
        name: 'Transportation',
        description: 'Gas, public transport, rideshare',
        icon: '🚗',
        color: '#3b82f6',
        type: 'expense',
        budget: 300,
        spent: 180
      },
      {
        id: 3,
        name: 'Shopping',
        description: 'Clothing, electronics, general shopping',
        icon: '🛒',
        color: '#f59e0b',
        type: 'expense',
        budget: 400,
        spent: 250
      },
      {
        id: 4,
        name: 'Utilities',
        description: 'Electricity, water, internet, phone',
        icon: '💡',
        color: '#ef4444',
        type: 'expense',
        budget: 200,
        spent: 180
      },
      {
        id: 5,
        name: 'Salary',
        description: 'Monthly salary and bonuses',
        icon: '💰',
        color: '#10b981',
        type: 'income'
      },
      {
        id: 6,
        name: 'Freelance',
        description: 'Freelance projects and consulting',
        icon: '💻',
        color: '#8b5cf6',
        type: 'income'
      }
    ];
  }

  get filteredCategories(): Category[] {
    if (this.selectedType === 'all') {
      return this.categories;
    }
    return this.categories.filter(cat => cat.type === this.selectedType);
  }

  openCategoryModal(): void {
    this.editingCategory = null;
    this.categoryForm.reset({
      icon: '🛒',
      color: '#3b82f6',
      type: 'expense',
      budget: 0
    });
    this.showModal = true;
  }

  editCategory(category: Category): void {
    this.editingCategory = category;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description,
      icon: category.icon,
      color: category.color,
      type: category.type,
      budget: category.budget || 0
    });
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingCategory = null;
    this.categoryForm.reset();
  }

  saveCategory(): void {
    if (this.categoryForm.valid) {
      this.isSaving = true;
      const formData = this.categoryForm.value;
      
      setTimeout(() => {
        if (this.editingCategory) {
          const index = this.categories.findIndex(c => c.id === this.editingCategory!.id);
          if (index !== -1) {
            this.categories[index] = { 
              ...this.editingCategory, 
              ...formData,
              spent: this.editingCategory.spent || 0
            };
          }
        } else {
          const newCategory: Category = {
            id: Math.max(...this.categories.map(c => c.id)) + 1,
            ...formData,
            spent: 0
          };
          this.categories.push(newCategory);
        }
        this.isSaving = false;
        this.closeModal();
      }, 1000);
    }
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categories = this.categories.filter(c => c.id !== id);
    }
  }

  getBudgetProgress(category: Category): number {
    if (!category.budget || category.budget === 0) return 0;
    return Math.min((category.spent || 0) / category.budget * 100, 100);
  }

  getBudgetStatus(category: Category): 'safe' | 'warning' | 'danger' {
    const progress = this.getBudgetProgress(category);
    if (progress >= 90) return 'danger';
    if (progress >= 75) return 'warning';
    return 'safe';
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  }
}