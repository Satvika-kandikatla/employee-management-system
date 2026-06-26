package com.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Employee Management API is running!  ");
        System.out.println("  URL: http://localhost:8080/api        ");
        System.out.println("========================================");
    }
}