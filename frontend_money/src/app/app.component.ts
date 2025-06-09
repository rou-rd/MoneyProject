import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, SidebarComponent, HeaderComponent, DashboardComponent],
  template: `
    <div class="app-layout">
      <app-sidebar></app-sidebar>
      <div class="main-content">
        <app-header></app-header>
        <app-dashboard></app-dashboard>
      </div>
    </div>
  `,
  styles: [`
    .app-layout {
      display: flex;
      min-height: 100vh;
      background-color: #f8fafc;
    }

    .main-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      margin-left: 280px;
    }

    @media (max-width: 1024px) {
      .main-content {
        margin-left: 0;
      }
    }
  `]
})
export class AppComponent {
  title = 'Mantis Dashboard';
}