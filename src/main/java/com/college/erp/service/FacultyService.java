package com.college.erp.service;

import com.college.erp.dto.FacultyDto;
import java.util.List;

public interface FacultyService {
    List<FacultyDto> getAllFaculty();
    FacultyDto getFacultyById(Long id);
    FacultyDto getFacultyByFacultyId(String facultyId);
    FacultyDto getFacultyByUserId(Long userId);
    FacultyDto createFaculty(FacultyDto facultyDto);
    FacultyDto updateFaculty(Long id, FacultyDto facultyDto);
    void deleteFaculty(Long id);
    List<FacultyDto> getFacultyByDepartment(String department);
}
