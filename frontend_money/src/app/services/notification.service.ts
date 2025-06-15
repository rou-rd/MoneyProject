import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message: string;
  duration?: number;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notifications$ = new BehaviorSubject<Notification[]>([]);
  public notifications = this.notifications$.asObservable();

  constructor() {}

  private generateId(): string {
    return Math.random().toString(36).substr(2, 9);
  }

  showSuccess(title: string, message: string, duration: number = 5000): void {
    this.addNotification({
      type: 'success',
      title,
      message,
      duration
    });
  }

  showError(title: string, message: string, duration: number = 7000): void {
    this.addNotification({
      type: 'error',
      title,
      message,
      duration
    });
  }

  showWarning(title: string, message: string, duration: number = 6000): void {
    this.addNotification({
      type: 'warning',
      title,
      message,
      duration
    });
  }

  showInfo(title: string, message: string, duration: number = 5000): void {
    this.addNotification({
      type: 'info',
      title,
      message,
      duration
    });
  }

  private addNotification(notification: Omit<Notification, 'id' | 'timestamp'>): void {
    const newNotification: Notification = {
      ...notification,
      id: this.generateId(),
      timestamp: new Date()
    };

    const currentNotifications = this.notifications$.value;
    this.notifications$.next([...currentNotifications, newNotification]);

    // Auto remove notification after duration
    if (notification.duration && notification.duration > 0) {
      setTimeout(() => {
        this.removeNotification(newNotification.id);
      }, notification.duration);
    }
  }

  removeNotification(id: string): void {
    const currentNotifications = this.notifications$.value;
    const filteredNotifications = currentNotifications.filter(n => n.id !== id);
    this.notifications$.next(filteredNotifications);
  }

  clearAll(): void {
    this.notifications$.next([]);
  }

  // Method to handle backend response messages
  handleApiResponse(response: any, successTitle: string = 'Success'): void {
    if (response && typeof response === 'string') {
      // Handle string responses from backend
      this.showSuccess(successTitle, response);
    } else if (response && response.message) {
      // Handle object responses with message property
      this.showSuccess(successTitle, response.message);
    } else {
      // Default success message
      this.showSuccess(successTitle, 'Operation completed successfully');
    }
  }

  handleApiError(error: any, defaultTitle: string = 'Error'): void {
    let errorMessage = 'An unexpected error occurred';
    let errorTitle = defaultTitle;

    if (error && error.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error.message) {
        errorMessage = error.error.message;
        errorTitle = error.error.title || defaultTitle;
      }
    } else if (error && error.message) {
      errorMessage = error.message;
    }

    this.showError(errorTitle, errorMessage);
  }
}