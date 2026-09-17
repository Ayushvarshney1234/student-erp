package com.college.erp.repository;

import com.college.erp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUserId(Long userId);
    List<Student> findByCourse(String course);
    List<Student> findBySemester(String semester);
    List<Student> findByCourseAndSemester(String course, String semester);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
}
