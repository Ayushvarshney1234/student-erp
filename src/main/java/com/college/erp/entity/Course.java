package com.college.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course ID is required")
    @Column(name = "course_id", nullable = false, unique = true, length = 30)
    private String courseId;

    @NotBlank(message = "Course Name is required")
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @NotBlank(message = "Course Code is required")
    @Column(name = "course_code", nullable = false, unique = true, length = 30)
    private String courseCode;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 100)
    private String department;

    @NotBlank(message = "Semester is required")
    @Column(nullable = false, length = 20)
    private String semester;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Column(nullable = false)
    private Integer credits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Course() {
    }

    public Course(Long id, String courseId, String courseName, String courseCode, String department, String semester, Integer credits, Faculty faculty) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.semester = semester;
        this.credits = credits;
        this.faculty = faculty;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
