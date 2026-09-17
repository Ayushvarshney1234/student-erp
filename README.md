# 🎓 College ERP Management System

A full-stack, enterprise-grade **College ERP Management System** built with **Java Spring Boot 3**, **Hibernate/JPA**, **MySQL Database**, **REST APIs**, and a modern responsive **Dashboard UI**.

---

## 🌟 Key Features

1. **Role-Based Access Control (RBAC)**:
   - **Admin**: Complete system control — Student, Faculty, Course, Attendance & Grade management.
   - **Faculty**: Class attendance marking, assigned course roster, exam grade entry.
   - **Student**: Personal profile, course schedule, attendance log & overall percentage, academic transcript.
2. **Student Directory**: Complete CRUD operations, course/semester filtering, demographic & contact management.
3. **Faculty Management**: Faculty profiles, department mapping, designation & assigned subjects.
4. **Course Catalog**: Course codes, credit units, semester allocation, and faculty assignment.
5. **Attendance Tracking**: Single & batch attendance marking, date-wise logs, attendance percentage computation, low attendance alerts (<75%).
6. **Academic Records & Transcripts**: Exam score entry, automatic grade & percentage calculation (`A+`, `A`, `B`, `C`, `F`), student CGPA transcript generation.
7. **Interactive Dashboard**: Real-time stats cards (Total Students, Faculty, Courses, Avg Attendance %), Chart.js graphs, and recent exam feed.
8. **API Testing Ready**: Includes pre-packaged **Postman Collection** (`College_ERP_Postman_Collection.json`) for instant API verification.

---

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.3 (Spring Web, Spring Data JPA, Spring Security, Spring Validation)
- **Database**: MySQL 8.0 (with H2 Database fallback profile for zero-config local runs)
- **ORM**: Hibernate / JPA
- **Frontend**: HTML5, CSS3 (Glassmorphism, Dark/Light theme), Vanilla JS, Chart.js, Font Awesome
- **API Format**: RESTful JSON with standardized HTTP status codes & global exception handling

---

## 📂 Project Architecture

```text
student-erp/
├── pom.xml
├── College_ERP_Postman_Collection.json
├── README.md
└── src/
    └── main/
        ├── java/com/college/erp/
        │   ├── CollegeErpApplication.java
        │   ├── config/
        │   │   ├── SecurityConfig.java
        │   │   └── DataInitializer.java
        │   ├── controller/
        │   │   ├── AuthController.java
        │   │   ├── StudentController.java
        │   │   ├── FacultyController.java
        │   │   ├── CourseController.java
        │   │   ├── AttendanceController.java
        │   │   ├── AcademicRecordController.java
        │   │   └── DashboardController.java
        │   ├── dto/
        │   │   ├── ApiResponse.java
        │   │   ├── LoginRequest.java / LoginResponse.java / RegisterRequest.java
        │   │   ├── StudentDto.java / FacultyDto.java / CourseDto.java
        │   │   ├── AttendanceDto.java / BatchAttendanceRequest.java
        │   │   ├── AcademicRecordDto.java
        │   │   └── DashboardStatsDto.java
        │   ├── entity/
        │   │   ├── User.java / Role.java
        │   │   ├── Student.java
        │   │   ├── Faculty.java
        │   │   ├── Course.java
        │   │   ├── Attendance.java
        │   │   └── AcademicRecord.java
        │   ├── exception/
        │   │   ├── ResourceNotFoundException.java
        │   │   ├── BadRequestException.java
        │   │   └── GlobalExceptionHandler.java
        │   ├── repository/
        │   │   ├── UserRepository.java
        │   │   ├── StudentRepository.java
        │   │   ├── FacultyRepository.java
        │   │   ├── CourseRepository.java
        │   │   ├── AttendanceRepository.java
        │   │   └── AcademicRecordRepository.java
        │   └── service/
        │       ├── AuthService.java / AuthServiceImpl.java
        │       ├── StudentService.java / StudentServiceImpl.java
        │       ├── FacultyService.java / FacultyServiceImpl.java
        │       ├── CourseService.java / CourseServiceImpl.java
        │       ├── AttendanceService.java / AttendanceServiceImpl.java
        │       ├── AcademicRecordService.java / AcademicRecordServiceImpl.java
        │       └── DashboardService.java / DashboardServiceImpl.java
        └── resources/
            ├── application.properties
            ├── application-mysql.properties
            ├── schema.sql
            ├── data.sql
            └── static/
                ├── index.html
                ├── css/style.css
                └── js/app.js
```

---

## 🛢️ Database Setup Instructions

### 1. MySQL Setup (Recommended for Production / Evaluation)
Create the MySQL database using MySQL Workbench or CLI:

```sql
CREATE DATABASE college_erp;
```

Update `application-mysql.properties` or set environment variables:
```properties
DB_URL=jdbc:mysql://localhost:3306/college_erp?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

Run with MySQL Profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 2. H2 Embedded Database Setup (Instant Zero-Config Testing)
No external database server is required! Simply start the application and Spring Boot uses in-memory H2 DB automatically.
- **H2 Web Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:college_erp`
- **User**: `sa` | **Password**: *(leave blank)*

---

## 🚀 How to Run Locally

1. **Prerequisites**: JDK 17+ and Maven 3.8+ installed.
2. **Compile the Project**:
   ```bash
   mvn clean package
   ```
3. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```
4. **Access Application**:
   Open browser at: `http://localhost:8080/`

---

## 🔑 Pre-Configured Demo Login Accounts

| Role | Username | Password | Email |
|---|---|---|---|
| **Admin** | `admin` | `password123` | `admin@college.edu` |
| **Faculty** | `dr_sharma` | `password123` | `sharma@college.edu` |
| **Student** | `rahul_kumar` | `password123` | `rahul.k@student.college.edu` |

*(Note: The web application UI includes a **Quick Role Switcher** bar at the top for instant 1-click role swapping!)*

---

## 📮 Postman REST API Documentation

Import `College_ERP_Postman_Collection.json` into Postman.

### Key REST Endpoints Summary:

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/login` | User login |
| `POST` | `/api/auth/register` | Register new user account |
| `GET` | `/api/students` | Get all students (Supports `?course=` & `?semester=`) |
| `POST` | `/api/students` | Create new student |
| `PUT` | `/api/students/{id}` | Update student details |
| `DELETE` | `/api/students/{id}` | Delete student record |
| `GET` | `/api/faculty` | Get all faculty members |
| `POST` | `/api/faculty` | Create faculty member |
| `GET` | `/api/courses` | Get course catalog |
| `POST` | `/api/courses` | Create new course |
| `POST` | `/api/courses/{cId}/assign-faculty/{fId}` | Assign faculty to course |
| `GET` | `/api/attendance` | View attendance logs |
| `POST` | `/api/attendance/batch` | Mark class batch attendance |
| `GET` | `/api/attendance/student/{id}/summary` | Get student attendance % summary |
| `GET` | `/api/academic-records` | Get exam scores & grades |
| `POST` | `/api/academic-records` | Record exam score |
| `GET` | `/api/academic-records/student/{id}/transcript` | Get full student CGPA transcript |
| `GET` | `/api/dashboard/stats` | Fetch real-time dashboard analytics |
