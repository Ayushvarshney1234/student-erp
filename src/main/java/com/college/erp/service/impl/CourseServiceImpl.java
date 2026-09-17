package com.college.erp.service.impl;

import com.college.erp.dto.CourseDto;
import com.college.erp.entity.Course;
import com.college.erp.entity.Faculty;
import com.college.erp.exception.BadRequestException;
import com.college.erp.exception.ResourceNotFoundException;
import com.college.erp.repository.CourseRepository;
import com.college.erp.repository.FacultyRepository;
import com.college.erp.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public CourseServiceImpl(CourseRepository courseRepository, FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        return convertToDto(course);
    }

    @Override
    public CourseDto getCourseByCourseId(String courseId) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with Course ID: " + courseId));
        return convertToDto(course);
    }

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        if (courseRepository.existsByCourseId(courseDto.getCourseId())) {
            throw new BadRequestException("Course ID already exists: " + courseDto.getCourseId());
        }
        if (courseRepository.existsByCourseCode(courseDto.getCourseCode())) {
            throw new BadRequestException("Course Code already exists: " + courseDto.getCourseCode());
        }

        Course course = convertToEntity(courseDto);
        if (courseDto.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(courseDto.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + courseDto.getFacultyId()));
            course.setFaculty(faculty);
        }

        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        existingCourse.setCourseName(courseDto.getCourseName());
        existingCourse.setDepartment(courseDto.getDepartment());
        existingCourse.setSemester(courseDto.getSemester());
        existingCourse.setCredits(courseDto.getCredits());

        if (courseDto.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(courseDto.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + courseDto.getFacultyId()));
            existingCourse.setFaculty(faculty);
        } else {
            existingCourse.setFaculty(null);
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        return convertToDto(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public CourseDto assignFacultyToCourse(Long courseId, Long facultyId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));

        course.setFaculty(faculty);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    @Override
    public List<CourseDto> getCoursesByFaculty(Long facultyId) {
        return courseRepository.findByFacultyId(facultyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseDto convertToDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseCode(),
                course.getDepartment(),
                course.getSemester(),
                course.getCredits(),
                course.getFaculty() != null ? course.getFaculty().getId() : null,
                course.getFaculty() != null ? course.getFaculty().getName() : null
        );
    }

    private Course convertToEntity(CourseDto dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setCourseId(dto.getCourseId());
        course.setCourseName(dto.getCourseName());
        course.setCourseCode(dto.getCourseCode());
        course.setDepartment(dto.getDepartment());
        course.setSemester(dto.getSemester());
        course.setCredits(dto.getCredits());
        return course;
    }
}
