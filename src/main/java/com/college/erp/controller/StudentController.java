package com.college.erp.controller;

import com.college.erp.dto.ApiResponse;
import com.college.erp.dto.StudentDto;
import com.college.erp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudents(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String semester) {
        List<StudentDto> students;
        if (course != null && semester != null) {
            students = studentService.getStudentsByCourseAndSemester(course, semester);
        } else {
            students = studentService.getAllStudents();
        }
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentById(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @GetMapping("/code/{studentId}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentByStudentId(@PathVariable String studentId) {
        StudentDto student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentByUserId(@PathVariable Long userId) {
        StudentDto student = studentService.getStudentByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Student profile retrieved", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDto>> createStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto created = studentService.createStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        StudentDto updated = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully"));
    }
}
