import { Component, OnInit, ChangeDetectorRef, NgZone, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { NavbarComponent } from '../navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from '../../services/auth.service';
import { Employee } from '../../models/employee.model';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [
    CommonModule, RouterLink, FormsModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatFormFieldModule,
    MatProgressSpinnerModule, MatSnackBarModule,
    MatTooltipModule, MatPaginatorModule,
    NavbarComponent, SidebarComponent
  ],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class EmployeeListComponent implements OnInit {
  // All employees from API
  allEmployees: Employee[] = [];
  // Filtered employees (after search)
  filteredEmployees: Employee[] = [];
  // Current page employees (shown in table)
  pagedEmployees: Employee[] = [];

  isLoading = true;
  searchKeyword = '';

  // Pagination settings
  pageSize = 5;
  pageIndex = 0;
  pageSizeOptions = [5, 10, 25];

  constructor(
    private employeeService: EmployeeService,
    public authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.isLoading = true;
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.ngZone.run(() => {
          this.allEmployees = data;
          this.filteredEmployees = data;
          this.updatePage();
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.ngZone.run(() => {
          console.error('Error:', err);
          this.isLoading = false;
          this.cdr.detectChanges();
        });
      }
    });
  }

  // Called when user types in search box
  onSearch(keyword: string): void {
    this.pageIndex = 0; // reset to first page
    if (!keyword || keyword.trim() === '') {
      this.filteredEmployees = [...this.allEmployees];
    } else {
      const lower = keyword.toLowerCase();
      this.filteredEmployees = this.allEmployees.filter(e =>
        e.name.toLowerCase().includes(lower) ||
        e.email.toLowerCase().includes(lower) ||
        e.department.toLowerCase().includes(lower)
      );
    }
    this.updatePage();
    this.cdr.detectChanges();
  }

  clearSearch(): void {
    this.searchKeyword = '';
    this.pageIndex = 0;
    this.filteredEmployees = [...this.allEmployees];
    this.updatePage();
    this.cdr.detectChanges();
  }

  // Called when paginator changes page or page size
  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePage();
  }

  // Slice the filtered array to show only current page
  updatePage(): void {
    const start = this.pageIndex * this.pageSize;
    const end = start + this.pageSize;
    this.pagedEmployees = this.filteredEmployees.slice(start, end);
  }

  deleteEmployee(id: number, name: string): void {
    if (!confirm(`Are you sure you want to delete "${name}"?`)) return;
    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.ngZone.run(() => {
          this.allEmployees = this.allEmployees.filter(e => e.id !== id);
          this.filteredEmployees = this.filteredEmployees.filter(e => e.id !== id);
          this.updatePage();
          this.cdr.detectChanges();
          this.showSnack(`"${name}" deleted successfully`, 'success');
        });
      },
      error: () => { this.showSnack('Failed to delete employee', 'error'); }
    });
  }

  private showSnack(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: type === 'success' ? ['snack-success'] : ['snack-error'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
  }

  get totalEmployees(): number {
    return this.filteredEmployees.length;
  }
}