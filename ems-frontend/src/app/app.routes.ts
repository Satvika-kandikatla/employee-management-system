import { Routes } from '@angular/router';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { EmployeeLoginComponent } from './components/employee-login/employee-login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { EmployeeProfileComponent } from './components/employee-profile/employee-profile.component';
import { authGuard, adminGuard } from './guards/auth.guard';
import { AttendanceComponent } from './components/attendance/attendance.component';

export const routes: Routes = [
  {
  path: 'attendance',
  component: AttendanceComponent,
  canActivate: [authGuard]
},
  // Default
  { path: '', redirectTo: '/admin/login', pathMatch: 'full' },

  // Public routes
  { path: 'admin/login', component: AdminLoginComponent },
  { path: 'employee/login', component: EmployeeLoginComponent },

  // Protected - any logged in user
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'employees',
    component: EmployeeListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'employees/profile/:id',
    component: EmployeeProfileComponent,
    canActivate: [authGuard]
  },

  // Protected - ADMIN only
  {
    path: 'employees/add',
    component: AddEmployeeComponent,
    canActivate: [adminGuard]
  },
  {
    path: 'employees/edit/:id',
    component: AddEmployeeComponent,
    canActivate: [adminGuard]
  },

  // Catch all
  { path: '**', redirectTo: '/admin/login' }
];