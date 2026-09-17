package com.college.erp.repository;

import com.college.erp.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    List<AcademicRecord> findByStudentId(Long studentId);
    List<AcademicRecord> findByCourseId(Long courseId);
    List<AcademicRecord> findByExamType(String examType);
    List<AcademicRecord> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT AVG(a.percentage) FROM AcademicRecord a WHERE a.student.id = :studentId")
    Double getAveragePercentageByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT AVG(a.percentage) FROM AcademicRecord a")
    Double getOverallAveragePercentage();

    List<AcademicRecord> findTop5ByOrderByIdDesc();
}
