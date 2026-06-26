import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NavbarComponent } from '../navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from '../../services/auth.service';
import { Employee } from '../../models/employee.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, RouterLink, MatCardModule,
    MatButtonModule, MatIconModule,
    MatProgressSpinnerModule, NavbarComponent, SidebarComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  stats = {
  totalEmployees: 0,
  totalDepartments: 0
};
  totalEmployees = 0;
  totalDepartments = 0;
  recentEmployees: Employee[] = [];
  isLoading = true;

  constructor(
    private employeeService: EmployeeService,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.ngZone.run(() => {
          this.recentEmployees = data;
          this.stats.totalEmployees = data.length;

const departments = new Set(
  data.map((e: Employee) => e.department)
);

this.stats.totalDepartments = departments.size;
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.ngZone.run(() => {
          console.error('Dashboard error:', err);
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      }
    });
  }
}