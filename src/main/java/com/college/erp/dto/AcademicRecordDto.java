package com.college.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AcademicRecordDto {

    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    private String studentCode;

    @NotNull(message = "Course ID is required")
    private Long courseId;
    private String courseName;
    private String courseCode;

    @NotBlank(message = "Exam type is required")
    private String examType;

    @NotNull(message = "Marks obtained is required")
    @Min(value = 0, message = "Marks cannot be negative")
    private Double marksObtained;

    @NotNull(message = "Max marks is required")
    @Min(value = 1, message = "Max marks must be greater than 0")
    private Double maxMarks;

    private Double percentage;
    private String grade;
    private String remarks;

    public AcademicRecordDto() {
    }

    public AcademicRecordDto(Long id, Long studentId, String studentName, String studentCode, Long courseId, String courseName, String courseCode, String examType, Double marksObtained, Double maxMarks, Double percentage, String grade, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.examType = examType;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.percentage = percentage;
        this.grade = grade;
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

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Double maxMarks) {
        this.maxMarks = maxMarks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
