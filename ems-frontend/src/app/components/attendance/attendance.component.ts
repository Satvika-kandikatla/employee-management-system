import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { NavbarComponent } from '../navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { AttendanceService } from '../../services/attendance.service';
import { AuthService } from '../../services/auth.service';
import { AttendanceTodayStatus, AttendanceRecord } from '../../models/employee.model';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [
    CommonModule, RouterLink,
    MatCardModule, MatButtonModule, MatIconModule,
    MatProgressSpinnerModule, MatSnackBarModule,
    MatTableModule, MatChipsModule, MatDividerModule,
    NavbarComponent, SidebarComponent
  ],
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class AttendanceComponent implements OnInit {
  todayStatus: AttendanceTodayStatus | null = null;
  attendanceHistory: AttendanceRecord[] = [];
  allTodayAttendance: AttendanceRecord[] = [];
  presentDays = 0;
  isLoading = false;
  isActionLoading = false;
  currentTime = new Date();

  displayedColumns = ['date', 'checkIn', 'checkOut', 'duration', 'status'];
  adminColumns = ['name', 'department', 'checkIn', 'checkOut', 'duration', 'status'];

  constructor(
    private attendanceService: AttendanceService,
    public authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    // Update clock every second
    setInterval(() => {
      this.ngZone.run(() => {
        this.currentTime = new Date();
        this.cdr.detectChanges();
      });
    }, 1000);

    this.loadData();
  }

  loadData(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.isLoading = true;

    // Get today's status
    this.attendanceService.getTodayStatus(userId).subscribe({
      next: (status) => {
        this.ngZone.run(() => {
          this.todayStatus = status;
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      },
      error: () => { this.isLoading = false; }
    });

    // Get attendance history
    this.attendanceService.getMyAttendance(userId).subscribe({
      next: (data) => {
        this.ngZone.run(() => {
          this.attendanceHistory = data;
          this.cdr.detectChanges();
        });
      }
    });

    // Get present days this month
    this.attendanceService.getPresentDays(userId).subscribe({
      next: (days) => {
        this.ngZone.run(() => {
          this.presentDays = days;
          this.cdr.detectChanges();
        });
      }
    });

    // If admin, get all today's attendance
    if (this.authService.isAdmin()) {
      this.attendanceService.getTodayAllAttendance().subscribe({
        next: (data) => {
          this.ngZone.run(() => {
            this.allTodayAttendance = data;
            this.cdr.detectChanges();
          });
        }
      });
    }
  }

  checkIn(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.isActionLoading = true;
    this.attendanceService.checkIn(userId).subscribe({
      next: (status) => {
        this.ngZone.run(() => {
          this.todayStatus = status;
          this.isActionLoading = false;
          this.showSnack(status.message, 'success');
          this.loadData();
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.ngZone.run(() => {
          this.isActionLoading = false;
          this.showSnack(err.error?.error || 'Check-in failed', 'error');
          this.cdr.detectChanges();
        });
      }
    });
  }

  checkOut(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.isActionLoading = true;
    this.attendanceService.checkOut(userId).subscribe({
      next: (status) => {
        this.ngZone.run(() => {
          this.todayStatus = status;
          this.isActionLoading = false;
          this.showSnack(status.message, 'success');
          this.loadData();
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.ngZone.run(() => {
          this.isActionLoading = false;
          this.showSnack(err.error?.error || 'Check-out failed', 'error');
          this.cdr.detectChanges();
        });
      }
    });
  }

  private showSnack(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      panelClass: type === 'success' ? ['snack-success'] : ['snack-error'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  formatTime(dateStr: string): string {
    if (!dateStr) return '--';
    const date = new Date(dateStr);
    return date.toLocaleTimeString('en-IN', {
      hour: '2-digit', minute: '2-digit', hour12: true
    });
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '--';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', {
      day: '2-digit', month: 'short', year: 'numeric'
    });
  }

  getStatusColor(status: string): string {
    switch(status) {
      case 'PRESENT': return 'primary';
      case 'HALF_DAY': return 'accent';
      case 'ABSENT': return 'warn';
      default: return 'primary';
    }
  }
}