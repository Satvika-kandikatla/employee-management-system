package com.employeemanagement.repository;

import com.employeemanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find today's attendance for an employee
    Optional<Attendance> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);

    // Get all attendance for an employee
    List<Attendance> findByEmployeeIdOrderByWorkDateDesc(Long employeeId);

    // Get all attendance for today (admin view)
    List<Attendance> findByWorkDateOrderByCheckInAsc(LocalDate workDate);

    // Get attendance for date range
    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId " +
           "AND a.workDate BETWEEN :startDate AND :endDate ORDER BY a.workDate DESC")
    List<Attendance> findByEmployeeAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Count present days this month
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId " +
           "AND MONTH(a.workDate) = MONTH(CURRENT_DATE) " +
           "AND YEAR(a.workDate) = YEAR(CURRENT_DATE)")
    long countPresentDaysThisMonth(@Param("employeeId") Long employeeId);
}