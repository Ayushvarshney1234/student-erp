package com.college.erp.dto;

import java.util.List;

public class DashboardStatsDto {

    private long totalStudents;
    private long totalFaculty;
    private long totalCourses;
    private double overallAttendancePercentage;
    private double averageAcademicPercentage;
    private List<AcademicRecordDto> recentAcademicRecords;
    private List<CourseDto> topCourses;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(long totalStudents, long totalFaculty, long totalCourses, double overallAttendancePercentage, double averageAcademicPercentage, List<AcademicRecordDto> recentAcademicRecords, List<CourseDto> topCourses) {
        this.totalStudents = totalStudents;
        this.totalFaculty = totalFaculty;
        this.totalCourses = totalCourses;
        this.overallAttendancePercentage = overallAttendancePercentage;
        this.averageAcademicPercentage = averageAcademicPercentage;
        this.recentAcademicRecords = recentAcademicRecords;
        this.topCourses = topCourses;
    }

    // Getters and Setters
    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalFaculty() {
        return totalFaculty;
    }

    public void setTotalFaculty(long totalFaculty) {
        this.totalFaculty = totalFaculty;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public double getOverallAttendancePercentage() {
        return overallAttendancePercentage;
    }

    public void setOverallAttendancePercentage(double overallAttendancePercentage) {
        this.overallAttendancePercentage = overallAttendancePercentage;
    }

    public double getAverageAcademicPercentage() {
        return averageAcademicPercentage;
    }

    public void setAverageAcademicPercentage(double averageAcademicPercentage) {
        this.averageAcademicPercentage = averageAcademicPercentage;
    }

    public List<AcademicRecordDto> getRecentAcademicRecords() {
        return recentAcademicRecords;
    }

    public void setRecentAcademicRecords(List<AcademicRecordDto> recentAcademicRecords) {
        this.recentAcademicRecords = recentAcademicRecords;
    }

    public List<CourseDto> getTopCourses() {
        return topCourses;
    }

    public void setTopCourses(List<CourseDto> topCourses) {
        this.topCourses = topCourses;
    }
}
