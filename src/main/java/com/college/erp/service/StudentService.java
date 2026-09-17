package com.college.erp.service;

import com.college.erp.dto.StudentDto;
import java.util.List;

public interface StudentService {
    List<StudentDto> getAllStudents();
    StudentDto getStudentById(Long id);
    StudentDto getStudentByStudentId(String studentId);
    StudentDto getStudentByUserId(Long userId);
    StudentDto createStudent(StudentDto studentDto);
    StudentDto updateStudent(Long id, StudentDto studentDto);
    void deleteStudent(Long id);
    List<StudentDto> getStudentsByCourseAndSemester(String course, String semester);
}
