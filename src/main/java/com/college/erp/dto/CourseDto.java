package com.college.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseDto {

    private Long id;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Course Name is required")
    private String courseName;

    @NotBlank(message = "Course Code is required")
    private String courseCode;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;

    private Long facultyId;
    private String facultyName;

    public CourseDto() {
    }

    public CourseDto(Long id, String courseId, String courseName, String courseCode, String department, String semester, Integer credits, Long facultyId, String facultyName) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.semester = semester;
        this.credits = credits;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
