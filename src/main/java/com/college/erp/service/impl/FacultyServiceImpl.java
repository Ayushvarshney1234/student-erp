package com.college.erp.service.impl;

import com.college.erp.dto.CourseDto;
import com.college.erp.dto.FacultyDto;
import com.college.erp.entity.Faculty;
import com.college.erp.entity.Role;
import com.college.erp.entity.User;
import com.college.erp.exception.BadRequestException;
import com.college.erp.exception.ResourceNotFoundException;
import com.college.erp.repository.CourseRepository;
import com.college.erp.repository.FacultyRepository;
import com.college.erp.repository.UserRepository;
import com.college.erp.service.FacultyService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              UserRepository userRepository,
                              CourseRepository courseRepository,
                              PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<FacultyDto> getAllFaculty() {
        return facultyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyDto getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));
        FacultyDto dto = convertToDto(faculty);
        dto.setAssignedCourses(getFacultyAssignedCourses(faculty.getId()));
        return dto;
    }

    @Override
    public FacultyDto getFacultyByFacultyId(String facultyId) {
        Faculty faculty = facultyRepository.findByFacultyId(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with Faculty ID: " + facultyId));
        return convertToDto(faculty);
    }

    @Override
    public FacultyDto getFacultyByUserId(Long userId) {
        Faculty faculty = facultyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found for User ID: " + userId));
        FacultyDto dto = convertToDto(faculty);
        dto.setAssignedCourses(getFacultyAssignedCourses(faculty.getId()));
        return dto;
    }

    @Override
    @Transactional
    public FacultyDto createFaculty(FacultyDto facultyDto) {
        if (facultyRepository.existsByFacultyId(facultyDto.getFacultyId())) {
            throw new BadRequestException("Faculty ID already exists: " + facultyDto.getFacultyId());
        }
        if (facultyRepository.existsByEmail(facultyDto.getEmail())) {
            throw new BadRequestException("Faculty email already exists: " + facultyDto.getEmail());
        }

        User user = null;
        if (facultyDto.getUserId() != null) {
            user = userRepository.findById(facultyDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + facultyDto.getUserId()));
        } else {
            String username = facultyDto.getFacultyId().toLowerCase();
            if (!userRepository.existsByUsername(username)) {
                user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("password123"));
                user.setEmail(facultyDto.getEmail());
                user.setRole(Role.FACULTY);
                user = userRepository.save(user);
            }
        }

        Faculty faculty = convertToEntity(facultyDto);
        faculty.setUser(user);
        Faculty savedFaculty = facultyRepository.save(faculty);
        return convertToDto(savedFaculty);
    }

    @Override
    @Transactional
    public FacultyDto updateFaculty(Long id, FacultyDto facultyDto) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));

        existingFaculty.setName(facultyDto.getName());
        existingFaculty.setDepartment(facultyDto.getDepartment());
        existingFaculty.setPhone(facultyDto.getPhone());
        existingFaculty.setDesignation(facultyDto.getDesignation());

        Faculty updatedFaculty = facultyRepository.save(existingFaculty);
        return convertToDto(updatedFaculty);
    }

    @Override
    @Transactional
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));
        facultyRepository.delete(faculty);
    }

    @Override
    public List<FacultyDto> getFacultyByDepartment(String department) {
        return facultyRepository.findByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private List<CourseDto> getFacultyAssignedCourses(Long facultyId) {
        return courseRepository.findByFacultyId(facultyId).stream()
                .map(c -> new CourseDto(
                        c.getId(),
                        c.getCourseId(),
                        c.getCourseName(),
                        c.getCourseCode(),
                        c.getDepartment(),
                        c.getSemester(),
                        c.getCredits(),
                        facultyId,
                        c.getFaculty() != null ? c.getFaculty().getName() : null
                )).collect(Collectors.toList());
    }

    private FacultyDto convertToDto(Faculty faculty) {
        return new FacultyDto(
                faculty.getId(),
                faculty.getFacultyId(),
                faculty.getName(),
                faculty.getEmail(),
                faculty.getDepartment(),
                faculty.getPhone(),
                faculty.getDesignation(),
                faculty.getUser() != null ? faculty.getUser().getId() : null
        );
    }

    private Faculty convertToEntity(FacultyDto dto) {
        Faculty faculty = new Faculty();
        faculty.setId(dto.getId());
        faculty.setFacultyId(dto.getFacultyId());
        faculty.setName(dto.getName());
        faculty.setEmail(dto.getEmail());
        faculty.setDepartment(dto.getDepartment());
        faculty.setPhone(dto.getPhone());
        faculty.setDesignation(dto.getDesignation());
        return faculty;
    }
}
