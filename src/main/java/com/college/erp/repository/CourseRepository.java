package com.college.erp.repository;

import com.college.erp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(String courseId);
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(String semester);
    List<Course> findByFacultyId(Long facultyId);
    boolean existsByCourseId(String courseId);
    boolean existsByCourseCode(String courseCode);
}
