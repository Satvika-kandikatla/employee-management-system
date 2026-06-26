package com.employeemanagement.controller;

import com.employeemanagement.dto.EmployeeDTO;
import com.employeemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<EmployeeDTO.LoginResponse> login(@RequestBody EmployeeDTO.LoginRequest req) {
        EmployeeDTO.LoginResponse response = employeeService.login(req);
        return response.isSuccess() ? ResponseEntity.ok(response) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO.Response> create(@RequestBody EmployeeDTO.Create dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(dto));
    }

    @GetMapping("/employees/search")
    public ResponseEntity<List<EmployeeDTO.Response>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO.Response>> getAll() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO.Response> update(@PathVariable Long id,
                                                        @RequestBody EmployeeDTO.Update dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Employee deleted successfully", "id", id));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<EmployeeDTO.DashboardStats> stats() {
        return ResponseEntity.ok(employeeService.getDashboardStats());
    } 
    // TEMPORARY - Remove after use
@GetMapping("/auth/hash/{password}")
public ResponseEntity<String> hashPassword(@PathVariable String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    return ResponseEntity.ok(encoder.encode(password));
}
}