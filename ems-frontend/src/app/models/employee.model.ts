export interface Employee {
  id: number;
  name: string;
  email: string;
  mobile: string;
  department: string;
  role: 'ADMIN' | 'EMPLOYEE';
  createdAt: string;
}

export interface CreateEmployee {
  name: string;
  email: string;
  mobile: string;
  department: string;
  password: string;
  role?: string;
}

export interface UpdateEmployee {
  name: string;
  email: string;
  mobile: string;
  department: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  name: string;
  email: string;
  role: string;
  message: string;
  success: boolean;
  token: string;
}

export interface DashboardStats {
  totalEmployees: number;
  totalDepartments: number;
}

export interface LoggedInUser {
  id: number;
  name: string;
  email: string;
  role: string;
}
export interface AttendanceTodayStatus {
  checkedIn: boolean;
  checkedOut: boolean;
  checkInTime: string;
  checkOutTime: string;
  duration: string;
  message: string;
}

export interface AttendanceRecord {
  id: number;
  employeeId: number;
  employeeName: string;
  employeeEmail: string;
  department: string;
  checkIn: string;
  checkOut: string;
  workDate: string;
  duration: string;
  status: string;
  checkedOut: boolean;
}