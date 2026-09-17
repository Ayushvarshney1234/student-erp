package com.college.erp.config;

import com.college.erp.entity.*;
import com.college.erp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AcademicRecordRepository academicRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           FacultyRepository facultyRepository,
                           CourseRepository courseRepository,
                           AttendanceRepository attendanceRepository,
                           AcademicRecordRepository academicRecordRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.academicRecordRepository = academicRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing default College ERP database records...");

            // 1. Create Default Users
            User adminUser = new User(null, "admin", passwordEncoder.encode("password123"), "admin@college.edu", Role.ADMIN, LocalDateTime.now());
            userRepository.save(adminUser);

            User facultyUser1 = new User(null, "dr_sharma", passwordEncoder.encode("password123"), "sharma@college.edu", Role.FACULTY, LocalDateTime.now());
            userRepository.save(facultyUser1);

            User facultyUser2 = new User(null, "prof_verma", passwordEncoder.encode("password123"), "verma@college.edu", Role.FACULTY, LocalDateTime.now());
            userRepository.save(facultyUser2);

            User studentUser1 = new User(null, "rahul_kumar", passwordEncoder.encode("password123"), "rahul.k@student.college.edu", Role.STUDENT, LocalDateTime.now());
            userRepository.save(studentUser1);

            User studentUser2 = new User(null, "ananya_singh", passwordEncoder.encode("password123"), "ananya.s@student.college.edu", Role.STUDENT, LocalDateTime.now());
            userRepository.save(studentUser2);

            User studentUser3 = new User(null, "rohan_gupta", passwordEncoder.encode("password123"), "rohan.g@student.college.edu", Role.STUDENT, LocalDateTime.now());
            userRepository.save(studentUser3);

            // 2. Create Faculty
            Faculty f1 = facultyRepository.save(new Faculty(null, "FAC2026001", "Dr. Rajesh Sharma", "sharma@college.edu", "Computer Science & Engineering", "+91 9876543210", "Professor & HOD", facultyUser1));
            Faculty f2 = facultyRepository.save(new Faculty(null, "FAC2026002", "Prof. Sunita Verma", "verma@college.edu", "Information Technology", "+91 9876543211", "Associate Professor", facultyUser2));

            // 3. Create Students
            Student s1 = studentRepository.save(new Student(null, "STU2026001", "Rahul Kumar", "rahul.k@student.college.edu", "+91 9123456780", LocalDate.of(2004, 5, 14), "Male", "BCA", "Semester 5", "12 MG Road, Bengaluru", studentUser1));
            Student s2 = studentRepository.save(new Student(null, "STU2026002", "Ananya Singh", "ananya.s@student.college.edu", "+91 9123456781", LocalDate.of(2004, 8, 22), "Female", "BCA", "Semester 5", "45 Park Street, Kolkata", studentUser2));
            Student s3 = studentRepository.save(new Student(null, "STU2026003", "Rohan Gupta", "rohan.g@student.college.edu", "+91 9123456782", LocalDate.of(2003, 11, 5), "Male", "B.Tech CSE", "Semester 5", "88 Civil Lines, Delhi", studentUser3));

            // 4. Create Courses
            Course c1 = courseRepository.save(new Course(null, "CRS101", "Data Structures & Algorithms", "CS301", "Computer Science & Engineering", "Semester 5", 4, f1));
            Course c2 = courseRepository.save(new Course(null, "CRS102", "Database Management Systems", "CS302", "Computer Science & Engineering", "Semester 5", 4, f1));
            Course c3 = courseRepository.save(new Course(null, "CRS103", "Web Application Development", "IT301", "Information Technology", "Semester 5", 3, f2));
            Course c4 = courseRepository.save(new Course(null, "CRS104", "Operating Systems", "CS303", "Computer Science & Engineering", "Semester 5", 4, f2));

            // 5. Create Attendance Records
            attendanceRepository.save(new Attendance(null, s1, c1, LocalDate.now().minusDays(5), "PRESENT", "On time"));
            attendanceRepository.save(new Attendance(null, s1, c1, LocalDate.now().minusDays(4), "PRESENT", "On time"));
            attendanceRepository.save(new Attendance(null, s1, c1, LocalDate.now().minusDays(3), "ABSENT", "Medical leave"));
            attendanceRepository.save(new Attendance(null, s1, c2, LocalDate.now().minusDays(5), "PRESENT", "Active participation"));
            attendanceRepository.save(new Attendance(null, s2, c1, LocalDate.now().minusDays(5), "PRESENT", "On time"));
            attendanceRepository.save(new Attendance(null, s2, c1, LocalDate.now().minusDays(4), "PRESENT", "On time"));
            attendanceRepository.save(new Attendance(null, s3, c1, LocalDate.now().minusDays(5), "LATE", "10 mins late"));
            attendanceRepository.save(new Attendance(null, s3, c2, LocalDate.now().minusDays(5), "PRESENT", "On time"));

            // 6. Create Academic Records / Marks
            academicRecordRepository.save(new AcademicRecord(null, s1, c1, "MID_TERM", 45.0, 50.0, 90.0, "A+", "Excellent analytical skills"));
            academicRecordRepository.save(new AcademicRecord(null, s1, c2, "MID_TERM", 42.5, 50.0, 85.0, "A", "Good database query optimization"));
            academicRecordRepository.save(new AcademicRecord(null, s2, c1, "MID_TERM", 48.0, 50.0, 96.0, "A+", "Top score in class"));
            academicRecordRepository.save(new AcademicRecord(null, s3, c1, "MID_TERM", 38.0, 50.0, 76.0, "B", "Needs practice in Graph algorithms"));

            log.info("Default seed data initialized successfully!");
        }
    }
}
