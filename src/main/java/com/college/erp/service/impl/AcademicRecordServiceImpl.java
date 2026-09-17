package com.college.erp.service.impl;

import com.college.erp.dto.AcademicRecordDto;
import com.college.erp.entity.AcademicRecord;
import com.college.erp.entity.Course;
import com.college.erp.entity.Student;
import com.college.erp.exception.ResourceNotFoundException;
import com.college.erp.repository.AcademicRecordRepository;
import com.college.erp.repository.CourseRepository;
import com.college.erp.repository.StudentRepository;
import com.college.erp.service.AcademicRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicRecordServiceImpl implements AcademicRecordService {

    private final AcademicRecordRepository academicRecordRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AcademicRecordServiceImpl(AcademicRecordRepository academicRecordRepository,
                                     StudentRepository studentRepository,
                                     CourseRepository courseRepository) {
        this.academicRecordRepository = academicRecordRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<AcademicRecordDto> getAllAcademicRecords() {
        return academicRecordRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AcademicRecordDto getAcademicRecordById(Long id) {
        AcademicRecord record = academicRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found with ID: " + id));
        return convertToDto(record);
    }

    @Override
    @Transactional
    public AcademicRecordDto addAcademicRecord(AcademicRecordDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + dto.getCourseId()));

        AcademicRecord record = new AcademicRecord();
        record.setStudent(student);
        record.setCourse(course);
        record.setExamType(dto.getExamType());
        record.setMarksObtained(dto.getMarksObtained());
        record.setMaxMarks(dto.getMaxMarks());
        record.setRemarks(dto.getRemarks());

        AcademicRecord saved = academicRecordRepository.save(record);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public AcademicRecordDto updateAcademicRecord(Long id, AcademicRecordDto dto) {
        AcademicRecord record = academicRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found with ID: " + id));

        record.setExamType(dto.getExamType());
        record.setMarksObtained(dto.getMarksObtained());
        record.setMaxMarks(dto.getMaxMarks());
        record.setRemarks(dto.getRemarks());

        AcademicRecord saved = academicRecordRepository.save(record);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public void deleteAcademicRecord(Long id) {
        AcademicRecord record = academicRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found with ID: " + id));
        academicRecordRepository.delete(record);
    }

    @Override
    public List<AcademicRecordDto> getAcademicRecordsByStudent(Long studentId) {
        return academicRecordRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AcademicRecordDto> getAcademicRecordsByCourse(Long courseId) {
        return academicRecordRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getStudentTranscript(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<AcademicRecord> records = academicRecordRepository.findByStudentId(studentId);

        double totalMarksObtained = records.stream().mapToDouble(AcademicRecord::getMarksObtained).sum();
        double totalMaxMarks = records.stream().mapToDouble(AcademicRecord::getMaxMarks).sum();
        double overallPercentage = totalMaxMarks > 0 ? Math.round((totalMarksObtained / totalMaxMarks) * 100.0 * 100.0) / 100.0 : 0.0;

        String cgpa = String.format("%.2f", overallPercentage / 10.0);

        Map<String, Object> transcript = new HashMap<>();
        transcript.put("studentId", student.getStudentId());
        transcript.put("studentName", student.getName());
        transcript.put("course", student.getCourse());
        transcript.put("semester", student.getSemester());
        transcript.put("totalExams", records.size());
        transcript.put("totalMarksObtained", totalMarksObtained);
        transcript.put("totalMaxMarks", totalMaxMarks);
        transcript.put("overallPercentage", overallPercentage);
        transcript.put("cgpa", cgpa);
        transcript.put("records", records.stream().map(this::convertToDto).collect(Collectors.toList()));

        return transcript;
    }

    private AcademicRecordDto convertToDto(AcademicRecord record) {
        return new AcademicRecordDto(
                record.getId(),
                record.getStudent().getId(),
                record.getStudent().getName(),
                record.getStudent().getStudentId(),
                record.getCourse().getId(),
                record.getCourse().getCourseName(),
                record.getCourse().getCourseCode(),
                record.getExamType(),
                record.getMarksObtained(),
                record.getMaxMarks(),
                record.getPercentage(),
                record.getGrade(),
                record.getRemarks()
        );
    }
}
