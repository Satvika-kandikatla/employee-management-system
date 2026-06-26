import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Employee, CreateEmployee, UpdateEmployee,
  LoginRequest, LoginResponse, DashboardStats
} from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private readonly API_URL = 'http://localhost:8080/api';

  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.API_URL}/auth/login`, credentials, { headers: this.headers }
    );
  }

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(
      `${this.API_URL}/employees`, { headers: this.headers }
    );
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(
      `${this.API_URL}/employees/${id}`, { headers: this.headers }
    );
  }

  createEmployee(employee: CreateEmployee): Observable<Employee> {
    return this.http.post<Employee>(
      `${this.API_URL}/employees`, employee, { headers: this.headers }
    );
  }

  updateEmployee(id: number, employee: UpdateEmployee): Observable<Employee> {
    return this.http.put<Employee>(
      `${this.API_URL}/employees/${id}`, employee, { headers: this.headers }
    );
  }

  deleteEmployee(id: number): Observable<any> {
    return this.http.delete(
      `${this.API_URL}/employees/${id}`, { headers: this.headers }
    );
  }

  searchEmployees(keyword: string): Observable<Employee[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<Employee[]>(
      `${this.API_URL}/employees/search`, { headers: this.headers, params }
    );
  }

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(
      `${this.API_URL}/dashboard/stats`, { headers: this.headers }
    );
  }
}