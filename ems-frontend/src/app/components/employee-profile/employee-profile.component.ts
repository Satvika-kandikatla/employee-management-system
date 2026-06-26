import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NavbarComponent } from '../navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from '../../services/auth.service';
import { Employee } from '../../models/employee.model';

@Component({
  selector: 'app-employee-profile',
  standalone: true,
  imports: [
    CommonModule, RouterLink, MatCardModule, MatButtonModule,
    MatIconModule, MatDividerModule, MatProgressSpinnerModule,
    NavbarComponent, SidebarComponent
  ],
  templateUrl: './employee-profile.component.html',
  styleUrls: ['./employee-profile.component.scss'],
})
export class EmployeeProfileComponent implements OnInit {
  employee: Employee | null = null;
  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.employeeService.getEmployeeById(id).subscribe({
      next: (data) => {
        this.ngZone.run(() => {
          this.employee = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      },
      error: () => {
        this.ngZone.run(() => {
          this.errorMessage = 'Employee not found.';
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      }
    });
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
  }

  getDeptColor(dept: string): string {
    const colors: Record<string, string> = {
      'Engineering': '#6A1B9A', 'Marketing': '#AD1457',
      'Human Resources': '#2E7D32', 'Finance': '#E65100',
      'Sales': '#00838F', 'Design': '#4527A0',
      'Operations': '#1565C0', 'Administration': '#37474F',
      'IT': '#0277BD', 'Legal': '#558B2F'
    };
    return colors[dept] || '#6A1B9A';
  }
}