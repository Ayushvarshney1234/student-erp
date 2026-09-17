package com.college.erp.service.impl;

import com.college.erp.dto.StudentDto;
import com.college.erp.entity.Role;
import com.college.erp.entity.Student;
import com.college.erp.entity.User;
import com.college.erp.exception.BadRequestException;
import com.college.erp.exception.ResourceNotFoundException;
import com.college.erp.repository.StudentRepository;
import com.college.erp.repository.UserRepository;
import com.college.erp.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return convertToDto(student);
    }

    @Override
    public StudentDto getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Student ID: " + studentId));
        return convertToDto(student);
    }

    @Override
    public StudentDto getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for User ID: " + userId));
        return convertToDto(student);
    }

    @Override
    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        if (studentRepository.existsByStudentId(studentDto.getStudentId())) {
            throw new BadRequestException("Student ID already exists: " + studentDto.getStudentId());
        }
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new BadRequestException("Student email already exists: " + studentDto.getEmail());
        }

        // Auto-create User account if not linked
        User user = null;
        if (studentDto.getUserId() != null) {
            user = userRepository.findById(studentDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + studentDto.getUserId()));
        } else {
            String username = studentDto.getStudentId().toLowerCase();
            if (!userRepository.existsByUsername(username)) {
                user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("password123"));
                user.setEmail(studentDto.getEmail());
                user.setRole(Role.STUDENT);
                user = userRepository.save(user);
            }
        }

        Student student = convertToEntity(studentDto);
        student.setUser(user);
        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        existingStudent.setName(studentDto.getName());
        existingStudent.setPhone(studentDto.getPhone());
        existingStudent.setDob(studentDto.getDob());
        existingStudent.setGender(studentDto.getGender());
        existingStudent.setCourse(studentDto.getCourse());
        existingStudent.setSemester(studentDto.getSemester());
        existingStudent.setAddress(studentDto.getAddress());

        Student updatedStudent = studentRepository.save(existingStudent);
        return convertToDto(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDto> getStudentsByCourseAndSemester(String course, String semester) {
        return studentRepository.findByCourseAndSemester(course, semester).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private StudentDto convertToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDob(),
                student.getGender(),
                student.getCourse(),
                student.getSemester(),
                student.getAddress(),
                student.getUser() != null ? student.getUser().getId() : null
        );
    }

    private Student convertToEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setStudentId(dto.getStudentId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDob(dto.getDob());
        student.setGender(dto.getGender());
        student.setCourse(dto.getCourse());
        student.setSemester(dto.getSemester());
        student.setAddress(dto.getAddress());
        return student;
    }
}
