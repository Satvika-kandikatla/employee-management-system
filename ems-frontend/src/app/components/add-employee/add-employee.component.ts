import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { NavbarComponent } from '../navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterLink,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSelectModule,
    MatProgressSpinnerModule, MatSnackBarModule,
    NavbarComponent, SidebarComponent
  ],
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.scss']
})
export class AddEmployeeComponent implements OnInit {
  employeeForm!: FormGroup;
  isLoading = false;
  isEditMode = false;
  employeeId: number | null = null;
  hidePassword = true;

  departments = [
    'Engineering', 'Marketing', 'Human Resources',
    'Finance', 'Sales', 'Design', 'Operations',
    'Administration', 'IT', 'Legal'
  ];

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.employeeId = this.route.snapshot.params['id']
      ? +this.route.snapshot.params['id'] : null;
    this.isEditMode = !!this.employeeId;
    this.buildForm();
    if (this.isEditMode && this.employeeId) {
      this.loadEmployee(this.employeeId);
    }
  }

  buildForm(): void {
    this.employeeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      department: ['', Validators.required],
      password: [
        { value: '', disabled: this.isEditMode },
        this.isEditMode ? [] : [Validators.required, Validators.minLength(6)]
      ]
    });
  }

  loadEmployee(id: number): void {
    this.isLoading = true;
    this.employeeService.getEmployeeById(id).subscribe({
      next: (emp) => {
        this.employeeForm.patchValue({
          name: emp.name, email: emp.email,
          mobile: emp.mobile, department: emp.department
        });
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; this.router.navigate(['/employees']); }
    });
  }

  get f() { return this.employeeForm.controls; }

  onSubmit(): void {
    if (this.employeeForm.invalid) return;
    this.isLoading = true;
    const formValue = this.employeeForm.getRawValue();

    if (this.isEditMode && this.employeeId) {
      const updateData = {
        name: formValue.name, email: formValue.email,
        mobile: formValue.mobile, department: formValue.department
      };
      this.employeeService.updateEmployee(this.employeeId, updateData).subscribe({
        next: () => {
          this.isLoading = false;
          this.showSnack('Employee updated successfully!', 'success');
          this.router.navigate(['/employees']);
        },
        error: (err) => {
          this.isLoading = false;
          this.showSnack(err.error?.error || 'Update failed', 'error');
        }
      });
    } else {
      this.employeeService.createEmployee({
        name: formValue.name, email: formValue.email,
        mobile: formValue.mobile, department: formValue.department,
        password: formValue.password
      }).subscribe({
        next: () => {
          this.isLoading = false;
          this.showSnack('Employee added successfully!', 'success');
          this.router.navigate(['/employees']);
        },
        error: (err) => {
          this.isLoading = false;
          this.showSnack(err.error?.error || 'Failed to add employee', 'error');
        }
      });
    }
  }

  private showSnack(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: type === 'success' ? ['snack-success'] : ['snack-error'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }
}