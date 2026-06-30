package com.employeemanagement.controller;

import com.employeemanagement.dto.AttendanceDTO;
import com.employeemanagement.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Employee checks in
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<AttendanceDTO.TodayStatus> checkIn(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    // Employee checks out
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<AttendanceDTO.TodayStatus> checkOut(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }

    // Get today's status for an employee
    @GetMapping("/today/{employeeId}")
    public ResponseEntity<AttendanceDTO.TodayStatus> getTodayStatus(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getTodayStatus(employeeId));
    }

    // Get attendance history for an employee
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<List<AttendanceDTO.Response>> getMyAttendance(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(employeeId));
    }

    // Admin: Get today's attendance for all employees
    @GetMapping("/today/all")
    public ResponseEntity<List<AttendanceDTO.Response>> getTodayAll() {
        return ResponseEntity.ok(attendanceService.getTodayAllAttendance());
    }

    // Get present days this month
    @GetMapping("/month/{employeeId}")
    public ResponseEntity<Long> getPresentDays(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getPresentDaysThisMonth(employeeId));
    }
}