package com.college.erp.controller;

import com.college.erp.dto.ApiResponse;
import com.college.erp.dto.AttendanceDto;
import com.college.erp.dto.BatchAttendanceRequest;
import com.college.erp.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceDto>>> getAllAttendance(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceDto> records;
        if (studentId != null) {
            records = attendanceService.getAttendanceByStudent(studentId);
        } else if (courseId != null) {
            records = attendanceService.getAttendanceByCourse(courseId);
        } else if (date != null) {
            records = attendanceService.getAttendanceByDate(date);
        } else {
            records = attendanceService.getAllAttendance();
        }
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved", records));
    }

    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentAttendanceSummary(@PathVariable Long studentId) {
        Map<String, Object> summary = attendanceService.getStudentAttendanceSummary(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student attendance summary fetched", summary));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceDto>> markAttendance(@Valid @RequestBody AttendanceDto attendanceDto) {
        AttendanceDto saved = attendanceService.markAttendance(attendanceDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attendance marked successfully", saved));
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<AttendanceDto>>> markBatchAttendance(@Valid @RequestBody BatchAttendanceRequest batchRequest) {
        List<AttendanceDto> savedList = attendanceService.markBatchAttendance(batchRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Batch attendance marked successfully", savedList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceDto>> updateAttendance(@PathVariable Long id, @Valid @RequestBody AttendanceDto attendanceDto) {
        AttendanceDto updated = attendanceService.updateAttendance(id, attendanceDto);
        return ResponseEntity.ok(ApiResponse.success("Attendance record updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance record deleted"));
    }
}
