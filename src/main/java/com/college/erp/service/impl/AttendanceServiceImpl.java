package com.college.erp.service.impl;

import com.college.erp.dto.AttendanceDto;
import com.college.erp.dto.BatchAttendanceRequest;
import com.college.erp.entity.Attendance;
import com.college.erp.entity.Course;
import com.college.erp.entity.Student;
import com.college.erp.exception.ResourceNotFoundException;
import com.college.erp.repository.AttendanceRepository;
import com.college.erp.repository.CourseRepository;
import com.college.erp.repository.StudentRepository;
import com.college.erp.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<AttendanceDto> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttendanceDto markAttendance(AttendanceDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + dto.getCourseId()));

        Optional<Attendance> existing = attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                dto.getStudentId(), dto.getCourseId(), dto.getAttendanceDate());

        Attendance attendance;
        if (existing.isPresent()) {
            attendance = existing.get();
            attendance.setStatus(dto.getStatus().toUpperCase());
            attendance.setRemarks(dto.getRemarks());
        } else {
            attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setAttendanceDate(dto.getAttendanceDate() != null ? dto.getAttendanceDate() : LocalDate.now());
            attendance.setStatus(dto.getStatus().toUpperCase());
            attendance.setRemarks(dto.getRemarks());
        }

        Attendance saved = attendanceRepository.save(attendance);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public List<AttendanceDto> markBatchAttendance(BatchAttendanceRequest batchRequest) {
        Course course = courseRepository.findById(batchRequest.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + batchRequest.getCourseId()));

        LocalDate date = batchRequest.getAttendanceDate() != null ? batchRequest.getAttendanceDate() : LocalDate.now();
        List<AttendanceDto> result = new ArrayList<>();

        if (batchRequest.getStudentAttendanceList() != null) {
            for (BatchAttendanceRequest.StudentAttendanceItem item : batchRequest.getStudentAttendanceList()) {
                Student student = studentRepository.findById(item.getStudentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + item.getStudentId()));

                Optional<Attendance> existing = attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                        student.getId(), course.getId(), date);

                Attendance attendance;
                if (existing.isPresent()) {
                    attendance = existing.get();
                    attendance.setStatus(item.getStatus().toUpperCase());
                    attendance.setRemarks(item.getRemarks());
                } else {
                    attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setCourse(course);
                    attendance.setAttendanceDate(date);
                    attendance.setStatus(item.getStatus().toUpperCase());
                    attendance.setRemarks(item.getRemarks());
                }

                Attendance saved = attendanceRepository.save(attendance);
                result.add(convertToDto(saved));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public AttendanceDto updateAttendance(Long id, AttendanceDto dto) {
        Attendance existing = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));

        existing.setStatus(dto.getStatus().toUpperCase());
        existing.setRemarks(dto.getRemarks());
        if (dto.getAttendanceDate() != null) {
            existing.setAttendanceDate(dto.getAttendanceDate());
        }

        Attendance saved = attendanceRepository.save(existing);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));
        attendanceRepository.delete(attendance);
    }

    @Override
    public List<AttendanceDto> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> getAttendanceByCourse(Long courseId) {
        return attendanceRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getStudentAttendanceSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<Attendance> records = attendanceRepository.findByStudentId(studentId);
        long total = records.size();
        long present = records.stream().filter(r -> "PRESENT".equalsIgnoreCase(r.getStatus())).count();
        long late = records.stream().filter(r -> "LATE".equalsIgnoreCase(r.getStatus())).count();
        long absent = records.stream().filter(r -> "ABSENT".equalsIgnoreCase(r.getStatus())).count();

        double percentage = total > 0 ? Math.round(((double) (present + late) / total) * 100.0 * 10.0) / 10.0 : 100.0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("studentId", student.getStudentId());
        summary.put("studentName", student.getName());
        summary.put("totalClasses", total);
        summary.put("present", present);
        summary.put("late", late);
        summary.put("absent", absent);
        summary.put("attendancePercentage", percentage);
        summary.put("records", records.stream().map(this::convertToDto).collect(Collectors.toList()));
        return summary;
    }

    private AttendanceDto convertToDto(Attendance attendance) {
        return new AttendanceDto(
                attendance.getId(),
                attendance.getStudent().getId(),
                attendance.getStudent().getName(),
                attendance.getStudent().getStudentId(),
                attendance.getCourse().getId(),
                attendance.getCourse().getCourseName(),
                attendance.getCourse().getCourseCode(),
                attendance.getAttendanceDate(),
                attendance.getStatus(),
                attendance.getRemarks()
        );
    }
}
