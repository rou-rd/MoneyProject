import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
/*hello*/
@Component({
  selector: 'app-metric-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="metric-card">
      <div class="metric-header">
        <h3 class="metric-title">{{ title }}</h3>
        <div class="metric-change" [class]="changeType">
          <svg *ngIf="changeType === 'positive'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="23,6 13.5,15.5 8.5,10.5 1,18"></polyline>
            <polyline points="17,6 23,6 23,12"></polyline>
          </svg>
          <svg *ngIf="changeType === 'negative'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="23,18 13.5,8.5 8.5,13.5 1,6"></polyline>
            <polyline points="17,18 23,18 23,12"></polyline>
          </svg>
          <span>{{ change }}</span>
        </div>
      </div>
      
      <div class="metric-value">{{ value }}</div>
      
      <div class="metric-subtitle">{{ subtitle }}</div>
    </div>
  `,
  styles: [`
    .metric-card {
      background: white;
      border-radius: 0.75rem;
      padding: 1.5rem;
      box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #e2e8f0;
      transition: all 0.2s ease;
    }

    .metric-card:hover {
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
      transform: translateY(-1px);
    }

    .metric-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
    }

    .metric-title {
      font-size: 0.875rem;
      font-weight: 500;
      color: #64748b;
      margin: 0;
    }

    .metric-change {
      display: flex;
      align-items: center;
      gap: 0.25rem;
      font-size: 0.875rem;
      font-weight: 600;
      padding: 0.25rem 0.5rem;
      border-radius: 0.375rem;
    }

    .metric-change.positive {
      color: #059669;
      background-color: #f0fdf4;
    }

    .metric-change.negative {
      color: #dc2626;
      background-color: #fef2f2;
    }

    .metric-value {
      font-size: 1.875rem;
      font-weight: 700;
      color: #1e293b;
      margin-bottom: 0.5rem;
      line-height: 1.2;
    }

    .metric-subtitle {
      font-size: 0.875rem;
      color: #64748b;
      line-height: 1.4;
    }

    @media (max-width: 640px) {
      .metric-card {
        padding: 1rem;
      }

      .metric-value {
        font-size: 1.5rem;
      }
    }
  `]
})
export class MetricCardComponent {
  @Input() title: string = '';
  @Input() value: string = '';
  @Input() change: string = '';
  @Input() changeType: 'positive' | 'negative' = 'positive';
  @Input() subtitle: string = '';
}