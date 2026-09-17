package com.college.erp.service;

import com.college.erp.dto.AcademicRecordDto;
import java.util.List;
import java.util.Map;

public interface AcademicRecordService {
    List<AcademicRecordDto> getAllAcademicRecords();
    AcademicRecordDto getAcademicRecordById(Long id);
    AcademicRecordDto addAcademicRecord(AcademicRecordDto recordDto);
    AcademicRecordDto updateAcademicRecord(Long id, AcademicRecordDto recordDto);
    void deleteAcademicRecord(Long id);
    List<AcademicRecordDto> getAcademicRecordsByStudent(Long studentId);
    List<AcademicRecordDto> getAcademicRecordsByCourse(Long courseId);
    Map<String, Object> getStudentTranscript(Long studentId);
}
