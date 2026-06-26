package com.employeemanagement.dto;

import com.employeemanagement.entity.Employee;
import java.time.LocalDateTime;

public class EmployeeDTO {

    // ── Response DTO ──────────────────────────────────────────────────
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private String mobile;
        private String department;
        private String role;
        private LocalDateTime createdAt;

        public Response() {}

        public static Response fromEntity(Employee emp) {
            Response r = new Response();
            r.id = emp.getId();
            r.name = emp.getName();
            r.email = emp.getEmail();
            r.mobile = emp.getMobile();
            r.department = emp.getDepartment();
            r.role = emp.getRole().name();
            r.createdAt = emp.getCreatedAt();
            return r;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }
        public String getDepartment() { return department; }
        public String getRole() { return role; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        // Builder
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long id) { r.id = id; return this; }
            public Builder name(String name) { r.name = name; return this; }
            public Builder email(String email) { r.email = email; return this; }
            public Builder mobile(String mobile) { r.mobile = mobile; return this; }
            public Builder department(String department) { r.department = department; return this; }
            public Builder role(String role) { r.role = role; return this; }
            public Builder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public Response build() { return r; }
        }
    }

    // ── Create DTO ────────────────────────────────────────────────────
    public static class Create {
        private String name;
        private String email;
        private String mobile;
        private String department;
        private String password;
        private String role;

        public Create() {}
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }
        public String getDepartment() { return department; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public void setDepartment(String department) { this.department = department; }
        public void setPassword(String password) { this.password = password; }
        public void setRole(String role) { this.role = role; }
    }

    // ── Update DTO ────────────────────────────────────────────────────
    public static class Update {
        private String name;
        private String email;
        private String mobile;
        private String department;

        public Update() {}
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }
        public String getDepartment() { return department; }
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public void setDepartment(String department) { this.department = department; }
    }

    // ── LoginRequest DTO ──────────────────────────────────────────────
    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;
    private boolean success;
    private String token;  // ← ADD THIS

    public LoginResponse() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
    public String getToken() { return token; }  // ← ADD THIS

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setMessage(String message) { this.message = message; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setToken(String token) { this.token = token; }  // ← ADD THIS

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final LoginResponse r = new LoginResponse();
        public Builder id(Long id) { r.id = id; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder email(String email) { r.email = email; return this; }
        public Builder role(String role) { r.role = role; return this; }
        public Builder message(String message) { r.message = message; return this; }
        public Builder success(boolean success) { r.success = success; return this; }
        public Builder token(String token) { r.token = token; return this; }  // ← ADD THIS
        public LoginResponse build() { return r; }
    }
}

    // ── DashboardStats DTO ────────────────────────────────────────────
    public static class DashboardStats {
        private long totalEmployees;
        private long totalDepartments;

        public DashboardStats() {}

        public long getTotalEmployees() { return totalEmployees; }
        public long getTotalDepartments() { return totalDepartments; }
        public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }
        public void setTotalDepartments(long totalDepartments) { this.totalDepartments = totalDepartments; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final DashboardStats s = new DashboardStats();
            public Builder totalEmployees(long v) { s.totalEmployees = v; return this; }
            public Builder totalDepartments(long v) { s.totalDepartments = v; return this; }
            public DashboardStats build() { return s; }
        }
    }
}