import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AttendanceTodayStatus, AttendanceRecord } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private readonly API_URL = 'http://localhost:8080/api/attendance';

  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  checkIn(employeeId: number): Observable<AttendanceTodayStatus> {
    return this.http.post<AttendanceTodayStatus>(
      `${this.API_URL}/check-in/${employeeId}`, {}, { headers: this.headers }
    );
  }

  checkOut(employeeId: number): Observable<AttendanceTodayStatus> {
    return this.http.post<AttendanceTodayStatus>(
      `${this.API_URL}/check-out/${employeeId}`, {}, { headers: this.headers }
    );
  }

  getTodayStatus(employeeId: number): Observable<AttendanceTodayStatus> {
    return this.http.get<AttendanceTodayStatus>(
      `${this.API_URL}/today/${employeeId}`
    );
  }

  getMyAttendance(employeeId: number): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(
      `${this.API_URL}/history/${employeeId}`
    );
  }

  getTodayAllAttendance(): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(
      `${this.API_URL}/today/all`
    );
  }

  getPresentDays(employeeId: number): Observable<number> {
    return this.http.get<number>(
      `${this.API_URL}/month/${employeeId}`
    );
  }
}