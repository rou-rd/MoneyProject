import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MetricCardComponent } from '../metric-card/metric-card.component';
import { ChartComponent } from '../chart/chart.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MetricCardComponent, ChartComponent],
  template: `
    <div class="dashboard">
      <div class="dashboard-content">
        <!-- Metrics Grid -->
        <div class="metrics-grid">
          <app-metric-card
            *ngFor="let metric of metrics"
            [title]="metric.title"
            [value]="metric.value"
            [change]="metric.change"
            [changeType]="metric.changeType"
            [subtitle]="metric.subtitle">
          </app-metric-card>
        </div>

        <!-- Charts Section -->
        <div class="charts-section">
          <div class="chart-container">
            <app-chart
              title="Unique Visitor"
              [data]="visitorChartData"
              type="area"
              [showControls]="true">
            </app-chart>
          </div>

          <div class="income-overview">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Income Overview</h3>
                <div class="time-controls">
                  <button class="time-btn">Month</button>
                  <button class="time-btn active">Week</button>
                </div>
              </div>
              <div class="card-content">
                <div class="income-stats">
                  <div class="stat-label">This Week Statistics</div>
                  <div class="stat-value">$7,650</div>
                </div>
                <app-chart
                  [data]="incomeChartData"
                  type="bar"
                  [showLegend]="false">
                </app-chart>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard {
      flex: 1;
      padding: 2rem;
      background-color: #f8fafc;
    }

    .dashboard-content {
      max-width: 1400px;
      margin: 0 auto;
    }

    .metrics-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }

    .charts-section {
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 1.5rem;
    }

    .chart-container {
      background: white;
      border-radius: 0.75rem;
      box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #e2e8f0;
    }

    .income-overview .card {
      background: white;
      border-radius: 0.75rem;
      box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #e2e8f0;
      height: 100%;
    }

    .card-header {
      padding: 1.5rem 1.5rem 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-title {
      font-size: 1.125rem;
      font-weight: 600;
      color: #1e293b;
      margin: 0;
    }

    .time-controls {
      display: flex;
      gap: 0.5rem;
    }

    .time-btn {
      padding: 0.375rem 0.75rem;
      border: 1px solid #e2e8f0;
      background: white;
      color: #64748b;
      border-radius: 0.375rem;
      font-size: 0.875rem;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .time-btn:hover {
      border-color: #3b82f6;
      color: #3b82f6;
    }

    .time-btn.active {
      background-color: #3b82f6;
      color: white;
      border-color: #3b82f6;
    }

    .card-content {
      padding: 1.5rem;
    }

    .income-stats {
      margin-bottom: 1.5rem;
    }

    .stat-label {
      font-size: 0.875rem;
      color: #64748b;
      margin-bottom: 0.25rem;
    }

    .stat-value {
      font-size: 1.875rem;
      font-weight: 700;
      color: #1e293b;
    }

    @media (max-width: 1024px) {
      .charts-section {
        grid-template-columns: 1fr;
      }
    }

    @media (max-width: 768px) {
      .dashboard {
        padding: 1rem;
      }

      .metrics-grid {
        grid-template-columns: 1fr;
        gap: 1rem;
      }

      .charts-section {
        gap: 1rem;
      }
    }
  `]
})
export class DashboardComponent implements OnInit {
  metrics = [
    {
      title: 'Total Page Views',
      value: '4,42,236',
      change: '59.3%',
      changeType: 'positive' as const,
      subtitle: 'You made an extra 35,000 this year'
    },
    {
      title: 'Total Users',
      value: '78,250',
      change: '70.5%',
      changeType: 'positive' as const,
      subtitle: 'You made an extra 8,900 this year'
    },
    {
      title: 'Total Order',
      value: '18,800',
      change: '27.4%',
      changeType: 'negative' as const,
      subtitle: 'You made an extra 1,943 this year'
    },
    {
      title: 'Total Sales',
      value: '$35,078',
      change: '27.4%',
      changeType: 'negative' as const,
      subtitle: 'You made an extra $20,395 this year'
    }
  ];

  visitorChartData = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [
      {
        label: 'Page Views',
        data: [45, 85, 65, 115, 75, 95, 135],
        borderColor: '#3b82f6',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        fill: true,
        tension: 0.4
      },
      {
        label: 'Sessions',
        data: [25, 45, 35, 65, 45, 55, 85],
        borderColor: '#1e40af',
        backgroundColor: 'rgba(30, 64, 175, 0.1)',
        fill: true,
        tension: 0.4
      }
    ]
  };

  incomeChartData = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [
      {
        data: [1200, 1900, 1400, 1600, 2400, 1800, 2200],
        backgroundColor: '#10b981',
        borderRadius: 4
      }
    ]
  };

  ngOnInit() {}
}