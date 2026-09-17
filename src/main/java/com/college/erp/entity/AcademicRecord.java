package com.college.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "academic_records")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Exam type is required")
    @Column(name = "exam_type", nullable = false, length = 50)
    private String examType; // MID_TERM, FINAL_EXAM, ASSIGNMENT, PRACTICAL

    @NotNull(message = "Marks obtained is required")
    @Min(value = 0, message = "Marks cannot be negative")
    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @NotNull(message = "Max marks is required")
    @Min(value = 1, message = "Max marks must be greater than 0")
    @Column(name = "max_marks", nullable = false)
    private Double maxMarks;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "grade", length = 5)
    private String grade;

    @Column(length = 255)
    private String remarks;

    public AcademicRecord() {
    }

    public AcademicRecord(Long id, Student student, Course course, String examType, Double marksObtained, Double maxMarks, Double percentage, String grade, String remarks) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.examType = examType;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.percentage = percentage;
        this.grade = grade;
        this.remarks = remarks;
    }

    @PrePersist
    @PreUpdate
    public void calculateGradeAndPercentage() {
        if (this.marksObtained != null && this.maxMarks != null && this.maxMarks > 0) {
            this.percentage = Math.round((this.marksObtained / this.maxMarks) * 100.0 * 100.0) / 100.0;
            if (this.percentage >= 90) {
                this.grade = "A+";
            } else if (this.percentage >= 80) {
                this.grade = "A";
            } else if (this.percentage >= 70) {
                this.grade = "B";
            } else if (this.percentage >= 60) {
                this.grade = "C";
            } else if (this.percentage >= 50) {
                this.grade = "D";
            } else {
                this.grade = "F";
            }
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
