package com.college.erp.service;

import com.college.erp.dto.CourseDto;
import java.util.List;

public interface CourseService {
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
    CourseDto getCourseByCourseId(String courseId);
    CourseDto createCourse(CourseDto courseDto);
    CourseDto updateCourse(Long id, CourseDto courseDto);
    void deleteCourse(Long id);
    CourseDto assignFacultyToCourse(Long courseId, Long facultyId);
    List<CourseDto> getCoursesByFaculty(Long facultyId);
    List<CourseDto> getCoursesByDepartment(String department);
}
