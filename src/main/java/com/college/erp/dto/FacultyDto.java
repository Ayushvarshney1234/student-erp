package com.college.erp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class FacultyDto {

    private Long id;

    @NotBlank(message = "Faculty ID is required")
    private String facultyId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String designation;
    private Long userId;
    private List<CourseDto> assignedCourses;

    public FacultyDto() {
    }

    public FacultyDto(Long id, String facultyId, String name, String email, String department, String phone, String designation, Long userId) {
        this.id = id;
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.designation = designation;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CourseDto> getAssignedCourses() {
        return assignedCourses;
    }

    public void setAssignedCourses(List<CourseDto> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }
}
