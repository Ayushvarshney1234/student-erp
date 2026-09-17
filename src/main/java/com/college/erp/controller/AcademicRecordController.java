package com.college.erp.controller;

import com.college.erp.dto.AcademicRecordDto;
import com.college.erp.dto.ApiResponse;
import com.college.erp.service.AcademicRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/academic-records")
@CrossOrigin(origins = "*")
public class AcademicRecordController {

    private final AcademicRecordService academicRecordService;

    public AcademicRecordController(AcademicRecordService academicRecordService) {
        this.academicRecordService = academicRecordService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicRecordDto>>> getAllAcademicRecords(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {
        List<AcademicRecordDto> records;
        if (studentId != null) {
            records = academicRecordService.getAcademicRecordsByStudent(studentId);
        } else if (courseId != null) {
            records = academicRecordService.getAcademicRecordsByCourse(courseId);
        } else {
            records = academicRecordService.getAllAcademicRecords();
        }
        return ResponseEntity.ok(ApiResponse.success("Academic records retrieved", records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicRecordDto>> getAcademicRecordById(@PathVariable Long id) {
        AcademicRecordDto record = academicRecordService.getAcademicRecordById(id);
        return ResponseEntity.ok(ApiResponse.success("Academic record retrieved", record));
    }

    @GetMapping("/student/{studentId}/transcript")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentTranscript(@PathVariable Long studentId) {
        Map<String, Object> transcript = academicRecordService.getStudentTranscript(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student transcript generated", transcript));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AcademicRecordDto>> addAcademicRecord(@Valid @RequestBody AcademicRecordDto recordDto) {
        AcademicRecordDto saved = academicRecordService.addAcademicRecord(recordDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Academic record added successfully", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicRecordDto>> updateAcademicRecord(@PathVariable Long id, @Valid @RequestBody AcademicRecordDto recordDto) {
        AcademicRecordDto updated = academicRecordService.updateAcademicRecord(id, recordDto);
        return ResponseEntity.ok(ApiResponse.success("Academic record updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAcademicRecord(@PathVariable Long id) {
        academicRecordService.deleteAcademicRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Academic record deleted successfully"));
    }
}
