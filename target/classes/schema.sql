-- College ERP System Schema Script
-- Supports both MySQL and H2

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    dob DATE,
    gender VARCHAR(10),
    course VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS faculty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    faculty_id VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    designation VARCHAR(50),
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_faculty_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(30) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    course_code VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    credits INT NOT NULL,
    faculty_id BIGINT,
    CONSTRAINT fk_course_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    remarks VARCHAR(255),
    CONSTRAINT fk_att_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_att_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS academic_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    exam_type VARCHAR(50) NOT NULL,
    marks_obtained DOUBLE NOT NULL,
    max_marks DOUBLE NOT NULL,
    percentage DOUBLE,
    grade VARCHAR(5),
    remarks VARCHAR(255),
    CONSTRAINT fk_rec_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
