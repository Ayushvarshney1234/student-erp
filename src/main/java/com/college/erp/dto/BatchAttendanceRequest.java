package com.college.erp.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class BatchAttendanceRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Attendance Date is required")
    private LocalDate attendanceDate;

    private List<StudentAttendanceItem> studentAttendanceList;

    public BatchAttendanceRequest() {
    }

    public static class StudentAttendanceItem {
        private Long studentId;
        private String status; // PRESENT, ABSENT, LATE
        private String remarks;

        public StudentAttendanceItem() {
        }

        public StudentAttendanceItem(Long studentId, String status, String remarks) {
            this.studentId = studentId;
            this.status = status;
            this.remarks = remarks;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
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

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public List<StudentAttendanceItem> getStudentAttendanceList() {
        return studentAttendanceList;
    }

    public void setStudentAttendanceList(List<StudentAttendanceItem> studentAttendanceList) {
        this.studentAttendanceList = studentAttendanceList;
    }
}
