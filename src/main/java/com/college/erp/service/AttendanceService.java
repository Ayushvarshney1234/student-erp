package com.college.erp.service;

import com.college.erp.dto.AttendanceDto;
import com.college.erp.dto.BatchAttendanceRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    List<AttendanceDto> getAllAttendance();
    AttendanceDto markAttendance(AttendanceDto attendanceDto);
    List<AttendanceDto> markBatchAttendance(BatchAttendanceRequest batchRequest);
    AttendanceDto updateAttendance(Long id, AttendanceDto attendanceDto);
    void deleteAttendance(Long id);
    List<AttendanceDto> getAttendanceByStudent(Long studentId);
    List<AttendanceDto> getAttendanceByCourse(Long courseId);
    List<AttendanceDto> getAttendanceByDate(LocalDate date);
    Map<String, Object> getStudentAttendanceSummary(Long studentId);
}
