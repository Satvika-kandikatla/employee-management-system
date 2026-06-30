package com.employeemanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceDTO {

    // Response DTO
    public static class Response {
        private Long id;
        private Long employeeId;
        private String employeeName;
        private String employeeEmail;
        private String department;
        private LocalDateTime checkIn;
        private LocalDateTime checkOut;
        private LocalDate workDate;
        private String duration;
        private String status;
        private boolean checkedOut;

        public Response() {}

        // Getters
        public Long getId() { return id; }
        public Long getEmployeeId() { return employeeId; }
        public String getEmployeeName() { return employeeName; }
        public String getEmployeeEmail() { return employeeEmail; }
        public String getDepartment() { return department; }
        public LocalDateTime getCheckIn() { return checkIn; }
        public LocalDateTime getCheckOut() { return checkOut; }
        public LocalDate getWorkDate() { return workDate; }
        public String getDuration() { return duration; }
        public String getStatus() { return status; }
        public boolean isCheckedOut() { return checkedOut; }

        // Setters
        public void setId(Long id) { this.id = id; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
        public void setDepartment(String department) { this.department = department; }
        public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
        public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
        public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
        public void setDuration(String duration) { this.duration = duration; }
        public void setStatus(String status) { this.status = status; }
        public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }

        // Builder
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long id) { r.id = id; return this; }
            public Builder employeeId(Long id) { r.employeeId = id; return this; }
            public Builder employeeName(String n) { r.employeeName = n; return this; }
            public Builder employeeEmail(String e) { r.employeeEmail = e; return this; }
            public Builder department(String d) { r.department = d; return this; }
            public Builder checkIn(LocalDateTime t) { r.checkIn = t; return this; }
            public Builder checkOut(LocalDateTime t) { r.checkOut = t; return this; }
            public Builder workDate(LocalDate d) { r.workDate = d; return this; }
            public Builder duration(String d) { r.duration = d; return this; }
            public Builder status(String s) { r.status = s; return this; }
            public Builder checkedOut(boolean b) { r.checkedOut = b; return this; }
            public Response build() { return r; }
        }
    }

    // Check-in request
    public static class CheckInRequest {
        private Long employeeId;
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    }

    // Today status response
    public static class TodayStatus {
        private boolean checkedIn;
        private boolean checkedOut;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private String duration;
        private String message;

        public TodayStatus() {}

        public boolean isCheckedIn() { return checkedIn; }
        public boolean isCheckedOut() { return checkedOut; }
        public LocalDateTime getCheckInTime() { return checkInTime; }
        public LocalDateTime getCheckOutTime() { return checkOutTime; }
        public String getDuration() { return duration; }
        public String getMessage() { return message; }

        public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }
        public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }
        public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
        public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
        public void setDuration(String duration) { this.duration = duration; }
        public void setMessage(String message) { this.message = message; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final TodayStatus s = new TodayStatus();
            public Builder checkedIn(boolean b) { s.checkedIn = b; return this; }
            public Builder checkedOut(boolean b) { s.checkedOut = b; return this; }
            public Builder checkInTime(LocalDateTime t) { s.checkInTime = t; return this; }
            public Builder checkOutTime(LocalDateTime t) { s.checkOutTime = t; return this; }
            public Builder duration(String d) { s.duration = d; return this; }
            public Builder message(String m) { s.message = m; return this; }
            public TodayStatus build() { return s; }
        }
    }
}