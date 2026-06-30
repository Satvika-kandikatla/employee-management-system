package com.employeemanagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "duration")
    private String duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AttendanceStatus {
        PRESENT, HALF_DAY, ABSENT
    }

    // Constructors
    public Attendance() {}

    // Getters
    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public LocalDate getWorkDate() { return workDate; }
    public String getDuration() { return duration; }
    public AttendanceStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
}
