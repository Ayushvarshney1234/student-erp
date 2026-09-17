package com.college.erp.repository;

import com.college.erp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByCourseId(Long courseId);
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
    List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);
    Optional<Attendance> findByStudentIdAndCourseIdAndAttendanceDate(Long studentId, Long courseId, LocalDate attendanceDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId")
    Long countTotalByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.status = 'PRESENT'")
    Long countPresentByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.status = 'PRESENT'")
    Long countTotalPresent();

    @Query("SELECT COUNT(a) FROM Attendance a")
    Long countTotalRecords();
}
