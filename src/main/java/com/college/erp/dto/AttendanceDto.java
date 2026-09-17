package com.college.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AttendanceDto {

    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    private String studentCode;

    @NotNull(message = "Course ID is required")
    private Long courseId;
    private String courseName;
    private String courseCode;

    @NotNull(message = "Attendance Date is required")
    private LocalDate attendanceDate;

    @NotBlank(message = "Status is required (PRESENT, ABSENT, LATE)")
    private String status;

    private String remarks;

    public AttendanceDto() {
    }

    public AttendanceDto(Long id, Long studentId, String studentName, String studentCode, Long courseId, String courseName, String courseCode, LocalDate attendanceDate, String status, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
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

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
