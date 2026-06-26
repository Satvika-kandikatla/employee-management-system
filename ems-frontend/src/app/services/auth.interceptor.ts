import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/**
 * JWT INTERCEPTOR
 * ---------------
 * Automatically adds the JWT token to every HTTP request.
 * So we don't have to add it manually in every service call.
 *
 * Before: GET /api/employees
 * After:  GET /api/employees + Header: Authorization: Bearer eyJhbGc...
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(authReq);
  }

  return next(req);
};