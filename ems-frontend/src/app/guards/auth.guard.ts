import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * AUTH GUARD - Checks if user is logged in
 * Redirects to login if not authenticated
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }
  router.navigate(['/admin/login']);
  return false;
};

/**
 * ADMIN GUARD - Checks if user is ADMIN
 * Redirects to dashboard if not admin
 */
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/admin/login']);
    return false;
  }

  if (!authService.isAdmin()) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};

/**
 * EMPLOYEE GUARD - Checks if user is EMPLOYEE
 * Redirects to dashboard if not employee
 */
export const employeeGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/employee/login']);
    return false;
  }

  return true;
};