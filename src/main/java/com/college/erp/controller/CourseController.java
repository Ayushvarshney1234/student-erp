package com.college.erp.controller;

import com.college.erp.dto.ApiResponse;
import com.college.erp.dto.CourseDto;
import com.college.erp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCourses(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Long facultyId) {
        List<CourseDto> courses;
        if (facultyId != null) {
            courses = courseService.getCoursesByFaculty(facultyId);
        } else if (department != null) {
            courses = courseService.getCoursesByDepartment(department);
        } else {
            courses = courseService.getAllCourses();
        }
        return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable Long id) {
        CourseDto course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully", course));
    }

    @GetMapping("/code/{courseId}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseByCourseId(@PathVariable String courseId) {
        CourseDto course = courseService.getCourseByCourseId(courseId);
        return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully", course));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDto created = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        CourseDto updated = courseService.updateCourse(id, courseDto);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", updated));
    }

    @PostMapping("/{courseId}/assign-faculty/{facultyId}")
    public ResponseEntity<ApiResponse<CourseDto>> assignFaculty(@PathVariable Long courseId, @PathVariable Long facultyId) {
        CourseDto updated = courseService.assignFacultyToCourse(courseId, facultyId);
        return ResponseEntity.ok(ApiResponse.success("Faculty assigned to course successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully"));
    }
}
