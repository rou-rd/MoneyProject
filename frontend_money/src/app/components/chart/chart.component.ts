import { Component, Input, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';

declare var Chart: any;

@Component({
  selector: 'app-chart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="chart-container">
      <div class="chart-header" *ngIf="title || showControls">
        <h3 class="chart-title" *ngIf="title">{{ title }}</h3>
        <div class="chart-controls" *ngIf="showControls">
          <button class="control-btn">Month</button>
          <button class="control-btn active">Week</button>
        </div>
      </div>
      <div class="chart-content">
        <canvas #chartCanvas></canvas>
        <div class="chart-legend" *ngIf="showLegend && type === 'area'">
          <div class="legend-item">
            <div class="legend-color" style="background-color: #3b82f6;"></div>
            <span>Page Views</span>
            <strong>48</strong>
          </div>
          <div class="legend-item">
            <div class="legend-color" style="background-color: #1e40af;"></div>
            <span>Sessions</span>
            <strong>24</strong>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .chart-container {
      padding: 1.5rem;
      height: 100%;
      display: flex;
      flex-direction: column;
    }

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1.5rem;
    }

    .chart-title {
      font-size: 1.125rem;
      font-weight: 600;
      color: #1e293b;
      margin: 0;
    }

    .chart-controls {
      display: flex;
      gap: 0.5rem;
    }

    .control-btn {
      padding: 0.375rem 0.75rem;
      border: 1px solid #e2e8f0;
      background: white;
      color: #64748b;
      border-radius: 0.375rem;
      font-size: 0.875rem;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .control-btn:hover {
      border-color: #3b82f6;
      color: #3b82f6;
    }

    .control-btn.active {
      background-color: #3b82f6;
      color: white;
      border-color: #3b82f6;
    }

    .chart-content {
      flex: 1;
      position: relative;
      min-height: 300px;
    }

    .chart-legend {
      display: flex;
      gap: 1.5rem;
      margin-top: 1rem;
      padding-top: 1rem;
      border-top: 1px solid #e2e8f0;
    }

    .legend-item {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.875rem;
    }

    .legend-color {
      width: 12px;
      height: 12px;
      border-radius: 50%;
    }

    .legend-item span {
      color: #64748b;
    }

    .legend-item strong {
      color: #1e293b;
      font-weight: 600;
    }

    canvas {
      max-height: 300px;
    }

    @media (max-width: 640px) {
      .chart-container {
        padding: 1rem;
      }

      .chart-header {
        flex-direction: column;
        gap: 1rem;
        align-items: flex-start;
      }

      .chart-legend {
        flex-direction: column;
        gap: 0.75rem;
      }
    }
  `]
})
export class ChartComponent implements OnInit, AfterViewInit {
  @Input() title: string = '';
  @Input() data: any = {};
  @Input() type: 'area' | 'bar' = 'area';
  @Input() showControls: boolean = false;
  @Input() showLegend: boolean = true;
  
  @ViewChild('chartCanvas', { static: true }) chartCanvas!: ElementRef<HTMLCanvasElement>;
  
  private chart: any;

  ngOnInit() {}

  ngAfterViewInit() {
    this.loadChartJS().then(() => {
      this.createChart();
    });
  }

  private async loadChartJS() {
    if (typeof Chart !== 'undefined') {
      return;
    }

    return new Promise<void>((resolve) => {
      const script = document.createElement('script');
      script.src = 'https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.js';
      script.onload = () => resolve();
      document.head.appendChild(script);
    });
  }

  private createChart() {
    const ctx = this.chartCanvas.nativeElement.getContext('2d');
    
    if (this.chart) {
      this.chart.destroy();
    }

    const config = this.type === 'area' ? this.getAreaChartConfig() : this.getBarChartConfig();
    
    this.chart = new Chart(ctx, config);
  }

  private getAreaChartConfig() {
    return {
      type: 'line',
      data: this.data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            border: {
              display: false
            }
          },
          y: {
            grid: {
              color: '#f1f5f9'
            },
            border: {
              display: false
            },
            ticks: {
              callback: function(value: any) {
                return value;
              }
            }
          }
        },
        elements: {
          point: {
            radius: 4,
            hoverRadius: 6
          }
        }
      }
    };
  }

  private getBarChartConfig() {
    return {
      type: 'bar',
      data: this.data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            border: {
              display: false
            }
          },
          y: {
            grid: {
              color: '#f1f5f9'
            },
            border: {
              display: false
            },
            ticks: {
              callback: function(value: any) {
                return '$' + value;
              }
            }
          }
        }
      }
    };
  }
}