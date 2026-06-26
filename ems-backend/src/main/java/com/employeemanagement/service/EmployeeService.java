package com.employeemanagement.service;

import com.employeemanagement.dto.EmployeeDTO;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public EmployeeService(EmployeeRepository employeeRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public EmployeeDTO.LoginResponse login(EmployeeDTO.LoginRequest request) {
        System.out.println("Login attempt: " + request.getEmail());

        Employee employee = employeeRepository.findByEmail(request.getEmail()).orElse(null);

        if (employee == null) {
            return EmployeeDTO.LoginResponse.builder()
                    .success(false).message("Invalid email or password").build();
        }

        // Check password using BCrypt
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), employee.getPassword());
        System.out.println("Password match: " + passwordMatch);

        if (!passwordMatch) {
            return EmployeeDTO.LoginResponse.builder()
                    .success(false).message("Invalid email or password").build();
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                employee.getEmail(),
                employee.getRole().name(),
                employee.getId()
        );

        return EmployeeDTO.LoginResponse.builder()
                .success(true)
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .role(employee.getRole().name())
                .message("Login successful")
                .token(token)
                .build();
    }

    // ── CREATE ────────────────────────────────────────────────────────
    public EmployeeDTO.Response createEmployee(EmployeeDTO.Create dto) {
        if (employeeRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());

        Employee.Role role = "ADMIN".equalsIgnoreCase(dto.getRole()) ?
                Employee.Role.ADMIN : Employee.Role.EMPLOYEE;

        Employee employee = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .mobile(dto.getMobile())
                .department(dto.getDepartment())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build();

        return EmployeeDTO.Response.fromEntity(employeeRepository.save(employee));
    }

    // ── READ ALL ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<EmployeeDTO.Response> getAllEmployees() {
        return employeeRepository.findByRole(Employee.Role.EMPLOYEE)
                .stream()
                .map(EmployeeDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // ── READ ONE ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public EmployeeDTO.Response getEmployeeById(Long id) {
        return EmployeeDTO.Response.fromEntity(
                employeeRepository.findById(id)
                        .orElseThrow(() -> new EmployeeNotFoundException(id)));
    }

    // ── UPDATE ────────────────────────────────────────────────────────
    public EmployeeDTO.Response updateEmployee(Long id, EmployeeDTO.Update dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (!employee.getEmail().equals(dto.getEmail()) &&
                employeeRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email already in use: " + dto.getEmail());

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setMobile(dto.getMobile());
        employee.setDepartment(dto.getDepartment());

        return EmployeeDTO.Response.fromEntity(employeeRepository.save(employee));
    }

    // ── DELETE ────────────────────────────────────────────────────────
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id))
            throw new EmployeeNotFoundException(id);
        employeeRepository.deleteById(id);
    }

    // ── SEARCH ────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<EmployeeDTO.Response> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword).stream()
                .filter(e -> e.getRole() == Employee.Role.EMPLOYEE)
                .map(EmployeeDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public EmployeeDTO.DashboardStats getDashboardStats() {
        return EmployeeDTO.DashboardStats.builder()
                .totalEmployees(employeeRepository.findByRole(Employee.Role.EMPLOYEE).size())
                .totalDepartments(employeeRepository.countDistinctDepartments())
                .build();
    }
}