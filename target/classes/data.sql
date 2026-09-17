-- College ERP Initial Seed Data
-- Note: Passwords are BCrypt hashed ('password123') or plain text for initial dev testing

-- Admin, Faculty, and Student Users
INSERT INTO users (id, username, password, email, role, created_at)
VALUES 
(1, 'admin', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'admin@college.edu', 'ADMIN', CURRENT_TIMESTAMP),
(2, 'dr_sharma', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'sharma@college.edu', 'FACULTY', CURRENT_TIMESTAMP),
(3, 'prof_verma', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'verma@college.edu', 'FACULTY', CURRENT_TIMESTAMP),
(4, 'rahul_kumar', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'rahul.k@student.college.edu', 'STUDENT', CURRENT_TIMESTAMP),
(5, 'ananya_singh', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'ananya.s@student.college.edu', 'STUDENT', CURRENT_TIMESTAMP),
(6, 'rohan_gupta', '$2a$10$e.w08s7ZJ6fN9q6C6GgYt.H9tU3M8H05G3p8c5T0.c7L2yT8M1iZe', 'rohan.g@student.college.edu', 'STUDENT', CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE id=id;

-- Faculty Profiles
INSERT INTO faculty (id, faculty_id, name, email, department, phone, designation, user_id)
VALUES
(1, 'FAC2026001', 'Dr. Rajesh Sharma', 'sharma@college.edu', 'Computer Science & Engineering', '+91 9876543210', 'Professor & HOD', 2),
(2, 'FAC2026002', 'Prof. Sunita Verma', 'verma@college.edu', 'Information Technology', '+91 9876543211', 'Associate Professor', 3)
ON DUPLICATE KEY UPDATE id=id;

-- Student Profiles
INSERT INTO students (id, student_id, name, email, phone, dob, gender, course, semester, address, user_id)
VALUES
(1, 'STU2026001', 'Rahul Kumar', 'rahul.k@student.college.edu', '+91 9123456780', '2004-05-14', 'Male', 'BCA', 'Semester 5', '12 MG Road, Bengaluru', 4),
(2, 'STU2026002', 'Ananya Singh', 'ananya.s@student.college.edu', '+91 9123456781', '2004-08-22', 'Female', 'BCA', 'Semester 5', '45 Park Street, Kolkata', 5),
(3, 'STU2026003', 'Rohan Gupta', 'rohan.g@student.college.edu', '+91 9123456782', '2003-11-05', 'Male', 'B.Tech CSE', 'Semester 5', '88 Civil Lines, Delhi', 6)
ON DUPLICATE KEY UPDATE id=id;

-- Courses
INSERT INTO courses (id, course_id, course_name, course_code, department, semester, credits, faculty_id)
VALUES
(1, 'CRS101', 'Data Structures & Algorithms', 'CS301', 'Computer Science & Engineering', 'Semester 5', 4, 1),
(2, 'CRS102', 'Database Management Systems', 'CS302', 'Computer Science & Engineering', 'Semester 5', 4, 1),
(3, 'CRS103', 'Web Application Development', 'IT301', 'Information Technology', 'Semester 5', 3, 2),
(4, 'CRS104', 'Operating Systems', 'CS303', 'Computer Science & Engineering', 'Semester 5', 4, 2)
ON DUPLICATE KEY UPDATE id=id;

-- Attendance Records
INSERT INTO attendance (id, student_id, course_id, attendance_date, status, remarks)
VALUES
(1, 1, 1, '2026-09-10', 'PRESENT', 'On time'),
(2, 1, 1, '2026-09-11', 'PRESENT', 'On time'),
(3, 1, 1, '2026-09-12', 'ABSENT', 'Medical leave'),
(4, 1, 2, '2026-09-10', 'PRESENT', 'Active participation'),
(5, 2, 1, '2026-09-10', 'PRESENT', 'On time'),
(6, 2, 1, '2026-09-11', 'PRESENT', 'On time'),
(7, 3, 1, '2026-09-10', 'LATE', '10 mins late'),
(8, 3, 2, '2026-09-10', 'PRESENT', 'On time')
ON DUPLICATE KEY UPDATE id=id;

-- Academic Records / Marks
INSERT INTO academic_records (id, student_id, course_id, exam_type, marks_obtained, max_marks, percentage, grade, remarks)
VALUES
(1, 1, 1, 'MID_TERM', 45.0, 50.0, 90.0, 'A+', 'Excellent analytical skills'),
(2, 1, 2, 'MID_TERM', 42.5, 50.0, 85.0, 'A', 'Good database query optimization'),
(3, 2, 1, 'MID_TERM', 48.0, 50.0, 96.0, 'A+', 'Top score in class'),
(4, 3, 1, 'MID_TERM', 38.0, 50.0, 76.0, 'B', 'Needs practice in Graph algorithms')
ON DUPLICATE KEY UPDATE id=id;
