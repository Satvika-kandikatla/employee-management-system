package com.employeemanagement.service;

import com.employeemanagement.dto.AttendanceDTO;
import com.employeemanagement.entity.Attendance;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.repository.AttendanceRepository;
import com.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // ── CHECK IN ─────────────────────────────────────────────────────
    public AttendanceDTO.TodayStatus checkIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        LocalDate today = LocalDate.now();
        Optional<Attendance> existing = attendanceRepository
                .findByEmployeeIdAndWorkDate(employeeId, today);

        if (existing.isPresent()) {
            Attendance att = existing.get();
            return AttendanceDTO.TodayStatus.builder()
                    .checkedIn(true)
                    .checkedOut(att.getCheckOut() != null)
                    .checkInTime(att.getCheckIn())
                    .checkOutTime(att.getCheckOut())
                    .duration(att.getDuration())
                    .message("Already checked in today at " + formatTime(att.getCheckIn()))
                    .build();
        }

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setCheckIn(LocalDateTime.now());
        attendance.setWorkDate(today);
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendanceRepository.save(attendance);

        return AttendanceDTO.TodayStatus.builder()
                .checkedIn(true)
                .checkedOut(false)
                .checkInTime(attendance.getCheckIn())
                .message("Check-in successful at " + formatTime(attendance.getCheckIn()))
                .build();
    }

    // ── CHECK OUT ────────────────────────────────────────────────────
    public AttendanceDTO.TodayStatus checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndWorkDate(employeeId, today)
                .orElseThrow(() -> new IllegalArgumentException("No check-in found for today. Please check in first."));

        if (attendance.getCheckOut() != null) {
            return AttendanceDTO.TodayStatus.builder()
                    .checkedIn(true)
                    .checkedOut(true)
                    .checkInTime(attendance.getCheckIn())
                    .checkOutTime(attendance.getCheckOut())
                    .duration(attendance.getDuration())
                    .message("Already checked out today at " + formatTime(attendance.getCheckOut()))
                    .build();
        }

        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOut(checkOutTime);

        // Calculate duration
        Duration duration = Duration.between(attendance.getCheckIn(), checkOutTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        String durationStr = hours + "h " + minutes + "m";
        attendance.setDuration(durationStr);

        // Set status based on hours worked
        if (hours < 4) {
            attendance.setStatus(Attendance.AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        }

        attendanceRepository.save(attendance);

        return AttendanceDTO.TodayStatus.builder()
                .checkedIn(true)
                .checkedOut(true)
                .checkInTime(attendance.getCheckIn())
                .checkOutTime(checkOutTime)
                .duration(durationStr)
                .message("Check-out successful! You worked " + durationStr)
                .build();
    }

    // ── GET TODAY STATUS ─────────────────────────────────────────────
    @Transactional(readOnly = true)
    public AttendanceDTO.TodayStatus getTodayStatus(Long employeeId) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> attendance = attendanceRepository
                .findByEmployeeIdAndWorkDate(employeeId, today);

        if (attendance.isEmpty()) {
            return AttendanceDTO.TodayStatus.builder()
                    .checkedIn(false)
                    .checkedOut(false)
                    .message("Not checked in yet today")
                    .build();
        }

        Attendance att = attendance.get();
        return AttendanceDTO.TodayStatus.builder()
                .checkedIn(true)
                .checkedOut(att.getCheckOut() != null)
                .checkInTime(att.getCheckIn())
                .checkOutTime(att.getCheckOut())
                .duration(att.getDuration())
                .message(att.getCheckOut() != null ?
                        "Checked out. Duration: " + att.getDuration() :
                        "Currently checked in")
                .build();
    }

    // ── GET MY ATTENDANCE HISTORY ────────────────────────────────────
    @Transactional(readOnly = true)
    public List<AttendanceDTO.Response> getMyAttendance(Long employeeId) {
        return attendanceRepository
                .findByEmployeeIdOrderByWorkDateDesc(employeeId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── GET TODAY ALL EMPLOYEES (ADMIN) ──────────────────────────────
    @Transactional(readOnly = true)
    public List<AttendanceDTO.Response> getTodayAllAttendance() {
        return attendanceRepository
                .findByWorkDateOrderByCheckInAsc(LocalDate.now())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── GET PRESENT COUNT THIS MONTH ─────────────────────────────────
    @Transactional(readOnly = true)
    public long getPresentDaysThisMonth(Long employeeId) {
        return attendanceRepository.countPresentDaysThisMonth(employeeId);
    }

    // ── HELPER METHODS ───────────────────────────────────────────────
    private AttendanceDTO.Response toResponse(Attendance att) {
        return AttendanceDTO.Response.builder()
                .id(att.getId())
                .employeeId(att.getEmployee().getId())
                .employeeName(att.getEmployee().getName())
                .employeeEmail(att.getEmployee().getEmail())
                .department(att.getEmployee().getDepartment())
                .checkIn(att.getCheckIn())
                .checkOut(att.getCheckOut())
                .workDate(att.getWorkDate())
                .duration(att.getDuration())
                .status(att.getStatus().name())
                .checkedOut(att.getCheckOut() != null)
                .build();
    }

    private String formatTime(LocalDateTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
}