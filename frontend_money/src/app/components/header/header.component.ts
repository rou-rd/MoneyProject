import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="header">
      <div class="header-content">
        <div class="header-left">
          <button class="menu-toggle">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="3" y1="6" x2="21" y2="6"></line>
              <line x1="3" y1="12" x2="21" y2="12"></line>
              <line x1="3" y1="18" x2="21" y2="18"></line>
            </svg>
          </button>
        </div>

        <div class="header-center">
          <div class="search-container">
            <svg class="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="m21 21-4.35-4.35"></path>
            </svg>
            <input type="text" placeholder="Ctrl + K" class="search-input">
            <span class="search-shortcut">Ctrl + K</span>
          </div>
        </div>

        <div class="header-right">
          <button class="header-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 19c-5 0-8-3-8-8s3-8 8-8 8 3 8 8-3 8-8 8z"></path>
              <path d="M17 8h4l-2-2 2-2h-4"></path>
            </svg>
          </button>
          
          <div class="notification-btn">
            <button class="header-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
                <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
              </svg>
              <span class="notification-badge">2</span>
            </button>
          </div>

          <button class="header-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
              <polyline points="16,17 21,12 16,7"></polyline>
              <line x1="21" y1="12" x2="9" y2="12"></line>
            </svg>
          </button>

          <div class="user-menu">
            <img src="https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&dpr=2" alt="John Doe" class="user-avatar">
            <span class="user-name">John Doe</span>
          </div>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .header {
      background: white;
      border-bottom: 1px solid #e2e8f0;
      padding: 0 2rem;
      height: 64px;
      display: flex;
      align-items: center;
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .header-content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
    }

    .header-left {
      display: flex;
      align-items: center;
    }

    .menu-toggle {
      display: none;
      background: none;
      border: none;
      padding: 0.5rem;
      color: #64748b;
      cursor: pointer;
      border-radius: 0.375rem;
    }

    .menu-toggle:hover {
      background-color: #f1f5f9;
    }

    .header-center {
      flex: 1;
      max-width: 400px;
      margin: 0 2rem;
    }

    .search-container {
      position: relative;
      display: flex;
      align-items: center;
    }

    .search-icon {
      position: absolute;
      left: 0.75rem;
      color: #94a3b8;
      z-index: 1;
    }

    .search-input {
      width: 100%;
      padding: 0.5rem 0.75rem 0.5rem 2.5rem;
      border: 1px solid #e2e8f0;
      border-radius: 0.5rem;
      background-color: #f8fafc;
      font-size: 0.875rem;
      outline: none;
      transition: all 0.2s ease;
    }

    .search-input:focus {
      border-color: #3b82f6;
      background-color: white;
      box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
    }

    .search-shortcut {
      position: absolute;
      right: 0.75rem;
      font-size: 0.75rem;
      color: #94a3b8;
      background-color: #e2e8f0;
      padding: 0.125rem 0.375rem;
      border-radius: 0.25rem;
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .header-btn {
      background: none;
      border: none;
      padding: 0.5rem;
      color: #64748b;
      cursor: pointer;
      border-radius: 0.375rem;
      transition: all 0.2s ease;
    }

    .header-btn:hover {
      background-color: #f1f5f9;
      color: #3b82f6;
    }

    .notification-btn {
      position: relative;
    }

    .notification-badge {
      position: absolute;
      top: 0.25rem;
      right: 0.25rem;
      background-color: #ef4444;
      color: white;
      font-size: 0.625rem;
      font-weight: 600;
      padding: 0.125rem 0.25rem;
      border-radius: 50%;
      min-width: 1rem;
      height: 1rem;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .user-menu {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.25rem;
      border-radius: 0.5rem;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .user-menu:hover {
      background-color: #f1f5f9;
    }

    .user-avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      object-fit: cover;
    }

    .user-name {
      font-weight: 500;
      color: #1e293b;
      font-size: 0.875rem;
    }

    @media (max-width: 1024px) {
      .menu-toggle {
        display: block;
      }

      .header-center {
        margin: 0 1rem;
      }

      .user-name {
        display: none;
      }
    }

    @media (max-width: 640px) {
      .header {
        padding: 0 1rem;
      }

      .header-center {
        display: none;
      }
    }
  `]
})
export class HeaderComponent {}