package com.college.erp.controller;

import com.college.erp.dto.ApiResponse;
import com.college.erp.dto.FacultyDto;
import com.college.erp.service.FacultyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacultyDto>>> getAllFaculty(@RequestParam(required = false) String department) {
        List<FacultyDto> facultyList;
        if (department != null) {
            facultyList = facultyService.getFacultyByDepartment(department);
        } else {
            facultyList = facultyService.getAllFaculty();
        }
        return ResponseEntity.ok(ApiResponse.success("Faculty members retrieved successfully", facultyList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyDto>> getFacultyById(@PathVariable Long id) {
        FacultyDto faculty = facultyService.getFacultyById(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty member retrieved successfully", faculty));
    }

    @GetMapping("/code/{facultyId}")
    public ResponseEntity<ApiResponse<FacultyDto>> getFacultyByFacultyId(@PathVariable String facultyId) {
        FacultyDto faculty = facultyService.getFacultyByFacultyId(facultyId);
        return ResponseEntity.ok(ApiResponse.success("Faculty member retrieved successfully", faculty));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<FacultyDto>> getFacultyByUserId(@PathVariable Long userId) {
        FacultyDto faculty = facultyService.getFacultyByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Faculty profile retrieved", faculty));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacultyDto>> createFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        FacultyDto created = facultyService.createFaculty(facultyDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Faculty member created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyDto>> updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyDto facultyDto) {
        FacultyDto updated = facultyService.updateFaculty(id, facultyDto);
        return ResponseEntity.ok(ApiResponse.success("Faculty member updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty member deleted successfully"));
    }
}
