import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface MenuItem {
  icon: string;
  label: string;
  active: boolean;
  section?: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <div class="logo-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            </svg>
          </div>
          <span class="logo-text">Mantis</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        <div class="nav-section" *ngFor="let section of menuSections">
          <div class="section-title" *ngIf="section.title">{{ section.title }}</div>
          <ul class="nav-list">
            <li *ngFor="let item of section.items" 
                class="nav-item" 
                [class.active]="item.active">
              <a href="#" class="nav-link">
                <span class="nav-icon" [innerHTML]="item.icon"></span>
                <span class="nav-label">{{ item.label }}</span>
              </a>
            </li>
          </ul>
        </div>
      </nav>

      <div class="sidebar-footer">
        <div class="user-profile">
          <div class="user-avatar">
            <img src="https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&dpr=2" alt="John Doe">
          </div>
          <div class="user-info">
            <div class="user-name">John Doe</div>
            <div class="user-role">Administrator</div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .sidebar {
      position: fixed;
      left: 0;
      top: 0;
      width: 280px;
      height: 100vh;
      background: white;
      border-right: 1px solid #e2e8f0;
      display: flex;
      flex-direction: column;
      z-index: 1000;
    }

    .sidebar-header {
      padding: 1.5rem;
      border-bottom: 1px solid #e2e8f0;
    }

    .logo {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }

    .logo-icon {
      width: 32px;
      height: 32px;
      background: linear-gradient(135deg, #3b82f6, #1d4ed8);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
    }

    .logo-text {
      font-size: 1.25rem;
      font-weight: 700;
      color: #1e293b;
    }

    .sidebar-nav {
      flex: 1;
      padding: 1rem 0;
      overflow-y: auto;
    }

    .nav-section {
      margin-bottom: 2rem;
    }

    .section-title {
      padding: 0 1.5rem 0.5rem;
      font-size: 0.75rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      color: #64748b;
    }

    .nav-list {
      list-style: none;
      padding-left: 0;
    }

    .nav-item {
      margin: 0.25rem 0;
    }

    .nav-link {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 0.75rem 1.5rem;
      color: #64748b;
      text-decoration: none;
      transition: all 0.2s ease;
      border-radius: 0;
    }

    .nav-link:hover {
      background-color: #f1f5f9;
      color: #3b82f6;
    }

    .nav-item.active .nav-link {
      background-color: #eff6ff;
      color: #3b82f6;
      border-right: 3px solid #3b82f6;
    }

    .nav-icon {
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .nav-label {
      font-weight: 500;
    }

    .sidebar-footer {
      padding: 1.5rem;
      border-top: 1px solid #e2e8f0;
    }

    .user-profile {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }

    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      overflow: hidden;
    }

    .user-avatar img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .user-name {
      font-weight: 600;
      color: #1e293b;
      font-size: 0.875rem;
    }

    .user-role {
      font-size: 0.75rem;
      color: #64748b;
    }

    @media (max-width: 1024px) {
      .sidebar {
        transform: translateX(-100%);
        transition: transform 0.3s ease;
      }
    }
  `]
})
export class SidebarComponent {
  menuSections = [
    {
      title: 'Dashboard',
      items: [
        { icon: '📊', label: 'Default', active: true }
      ]
    },
    {
      title: 'Authentication',
      items: [
        { icon: '🔐', label: 'Login', active: false },
        { icon: '📝', label: 'Register', active: false }
      ]
    },
    {
      title: 'UI Components',
      items: [
        { icon: '🔤', label: 'Typography', active: false },
        { icon: '🎨', label: 'Colors', active: false },
        { icon: '⚡', label: 'Ant Icons', active: false }
      ]
    },
    {
      title: 'Other',
      items: [
        { icon: '📄', label: 'Sample Page', active: false },
        { icon: '📚', label: 'Document', active: false }
      ]
    }
  ];
}
