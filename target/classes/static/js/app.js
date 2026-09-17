/* ==========================================================================
   AcademiaERP - Core JavaScript Frontend Engine
   ========================================================================== */

const API_BASE = '/api';

// Application State
const state = {
    currentRole: 'ADMIN',
    currentUser: {
        userId: 1,
        username: 'admin',
        name: 'System Administrator',
        role: 'ADMIN',
        profileId: null
    },
    students: [],
    faculty: [],
    courses: [],
    attendance: [],
    academicRecords: [],
    charts: {}
};

// Initialize Application on DOM Ready
document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    setupEventListeners();
    switchRole('ADMIN'); // Default demo role: ADMIN
});

// Theme Management
function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
}

document.getElementById('themeToggleBtn')?.addEventListener('click', () => {
    const current = document.documentElement.getAttribute('data-theme');
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('theme', next);
    showToast(`Switched to ${next} mode`, 'success');
});

// Event Listeners Setup
function setupEventListeners() {
    // Navigation Tabs
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const tab = link.getAttribute('data-tab');
            switchTab(tab);
        });
    });

    // Quick Role Buttons
    document.querySelectorAll('.demo-role-selector .role-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const role = btn.getAttribute('data-role');
            switchRole(role);
        });
    });

    // Sidebar Mobile Toggle
    document.getElementById('sidebarToggleBtn')?.addEventListener('click', () => {
        document.getElementById('sidebar')?.classList.toggle('active');
    });

    document.getElementById('sidebarCloseBtn')?.addEventListener('click', () => {
        document.getElementById('sidebar')?.classList.remove('active');
    });

    // Search and Filter Listeners
    document.getElementById('studentSearchInput')?.addEventListener('input', filterStudents);
    document.getElementById('studentCourseFilter')?.addEventListener('change', filterStudents);
    document.getElementById('studentSemesterFilter')?.addEventListener('change', filterStudents);

    document.getElementById('facultySearchInput')?.addEventListener('input', filterFaculty);
    document.getElementById('facultyDeptFilter')?.addEventListener('change', filterFaculty);

    document.getElementById('globalSearchInput')?.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') {
            const query = e.target.value.toLowerCase().trim();
            if (query) {
                switchTab('students');
                document.getElementById('studentSearchInput').value = query;
                filterStudents();
            }
        }
    });

    // Form Submit Handlers
    document.getElementById('studentForm')?.addEventListener('submit', handleStudentSubmit);
    document.getElementById('facultyForm')?.addEventListener('submit', handleFacultySubmit);
    document.getElementById('courseForm')?.addEventListener('submit', handleCourseSubmit);
    document.getElementById('batchAttendanceForm')?.addEventListener('submit', handleBatchAttendanceSubmit);
    document.getElementById('academicForm')?.addEventListener('submit', handleAcademicSubmit);
}

// Role Switcher Logic
function switchRole(role) {
    state.currentRole = role;

    // Update Top Navbar Buttons
    document.querySelectorAll('.demo-role-selector .role-btn').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-role') === role);
    });

    // Update User Pill Info
    const userRoleEl = document.getElementById('currentUserRole');
    const userNameEl = document.getElementById('currentUserName');
    const welcomeNameEl = document.getElementById('welcomeName');
    const avatarEl = document.getElementById('userAvatar');
    const topAvatarEl = document.getElementById('topAvatar');

    if (role === 'ADMIN') {
        state.currentUser = { userId: 1, username: 'admin', name: 'System Administrator', role: 'ADMIN', profileId: null };
    } else if (role === 'FACULTY') {
        state.currentUser = { userId: 2, username: 'dr_sharma', name: 'Dr. Rajesh Sharma', role: 'FACULTY', profileId: 1 };
    } else if (role === 'STUDENT') {
        state.currentUser = { userId: 4, username: 'rahul_kumar', name: 'Rahul Kumar', role: 'STUDENT', profileId: 1 };
    }

    if (userRoleEl) userRoleEl.textContent = role;
    if (userNameEl) userNameEl.textContent = state.currentUser.name;
    if (welcomeNameEl) welcomeNameEl.textContent = state.currentUser.name;
    if (avatarEl) avatarEl.textContent = state.currentUser.name.charAt(0);
    if (topAvatarEl) topAvatarEl.textContent = state.currentUser.name.charAt(0);

    // Permission Based UI Toggles
    applyRolePermissions();

    showToast(`Logged in as ${role} (${state.currentUser.name})`, 'success');
    loadDashboardStats();
}

function applyRolePermissions() {
    const isStudent = state.currentRole === 'STUDENT';
    const isFaculty = state.currentRole === 'FACULTY';
    const isAdmin = state.currentRole === 'ADMIN';

    // Hide/Show Admin-only Navigation & Action Buttons
    document.querySelectorAll('.header-actions button').forEach(btn => {
        btn.style.display = isStudent ? 'none' : 'inline-flex';
    });
}

// Navigation Tab Switcher
function switchTab(tabId) {
    document.querySelectorAll('.tab-view').forEach(view => view.classList.remove('active'));
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));

    const targetView = document.getElementById(`tab-${tabId}`);
    const targetLink = document.querySelector(`.nav-link[data-tab="${tabId}"]`);

    if (targetView) targetView.classList.add('active');
    if (targetLink) targetLink.classList.add('active');

    // Close Mobile Sidebar
    document.getElementById('sidebar')?.classList.remove('active');

    // Load Tab Specific Data
    switch (tabId) {
        case 'dashboard':
            loadDashboardStats();
            break;
        case 'students':
            loadStudents();
            break;
        case 'faculty':
            loadFaculty();
            break;
        case 'courses':
            loadCourses();
            break;
        case 'attendance':
            loadAttendanceRecords();
            break;
        case 'academics':
            loadAcademicRecords();
            break;
    }
}

// REST API Fetch Handlers

// 1. Dashboard Stats
async function loadDashboardStats() {
    try {
        const res = await fetch(`${API_BASE}/dashboard/stats`);
        const json = await res.json();
        if (json.success) {
            const d = json.data;
            document.getElementById('statStudents').textContent = d.totalStudents;
            document.getElementById('statFaculty').textContent = d.totalFaculty;
            document.getElementById('statCourses').textContent = d.totalCourses;
            document.getElementById('statAttendance').textContent = `${d.overallAttendancePercentage}%`;

            renderRecentMarksTable(d.recentAcademicRecords || []);
            renderCharts(d);
        }
    } catch (err) {
        console.error('Failed to load dashboard stats:', err);
    }
}

// 2. Students CRUD
async function loadStudents() {
    try {
        const res = await fetch(`${API_BASE}/students`);
        const json = await res.json();
        if (json.success) {
            state.students = json.data;
            renderStudentsTable(state.students);
        }
    } catch (err) {
        showToast('Error loading student records', 'error');
    }
}

function renderStudentsTable(students) {
    const tbody = document.getElementById('studentTableBody');
    if (!tbody) return;

    if (students.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center">No student records found.</td></tr>`;
        return;
    }

    tbody.innerHTML = students.map(s => `
        <tr>
            <td><strong>${s.studentId}</strong></td>
            <td>${s.name}</td>
            <td>${s.email}</td>
            <td>${s.phone}</td>
            <td><span class="badge">${s.course}</span></td>
            <td>${s.semester}</td>
            <td>
                <div class="table-actions">
                    <button class="btn-action" title="Edit" onclick="editStudent(${s.id})"><i class="fa-solid fa-pen"></i></button>
                    ${state.currentRole === 'ADMIN' ? `<button class="btn-action delete" title="Delete" onclick="deleteStudent(${s.id})"><i class="fa-solid fa-trash"></i></button>` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

function filterStudents() {
    const query = document.getElementById('studentSearchInput')?.value.toLowerCase().trim() || '';
    const course = document.getElementById('studentCourseFilter')?.value || '';
    const semester = document.getElementById('studentSemesterFilter')?.value || '';

    const filtered = state.students.filter(s => {
        const matchesQuery = s.name.toLowerCase().includes(query) || s.studentId.toLowerCase().includes(query) || s.email.toLowerCase().includes(query);
        const matchesCourse = !course || s.course === course;
        const matchesSem = !semester || s.semester === semester;
        return matchesQuery && matchesCourse && matchesSem;
    });

    renderStudentsTable(filtered);
}

function openStudentModal(student = null) {
    document.getElementById('studentModalTitle').textContent = student ? 'Edit Student' : 'Add New Student';
    document.getElementById('studentIdField').value = student ? student.id : '';
    document.getElementById('stuCode').value = student ? student.studentId : `STU${Date.now().toString().slice(-6)}`;
    document.getElementById('stuName').value = student ? student.name : '';
    document.getElementById('stuEmail').value = student ? student.email : '';
    document.getElementById('stuPhone').value = student ? student.phone : '';
    document.getElementById('stuCourse').value = student ? student.course : 'BCA';
    document.getElementById('stuSemester').value = student ? student.semester : 'Semester 5';
    document.getElementById('stuDob').value = student ? student.dob : '';
    document.getElementById('stuGender').value = student ? student.gender : 'Female';
    document.getElementById('stuAddress').value = student ? student.address || '' : '';

    showModal('modal-student');
}

async function editStudent(id) {
    const student = state.students.find(s => s.id === id);
    if (student) openStudentModal(student);
}

async function handleStudentSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('studentIdField').value;
    const payload = {
        studentId: document.getElementById('stuCode').value,
        name: document.getElementById('stuName').value,
        email: document.getElementById('stuEmail').value,
        phone: document.getElementById('stuPhone').value,
        course: document.getElementById('stuCourse').value,
        semester: document.getElementById('stuSemester').value,
        dob: document.getElementById('stuDob').value || null,
        gender: document.getElementById('stuGender').value,
        address: document.getElementById('stuAddress').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/students/${id}` : `${API_BASE}/students`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.success) {
            showToast(json.message, 'success');
            closeModal('modal-student');
            loadStudents();
            loadDashboardStats();
        } else {
            showToast(json.message || 'Operation failed', 'error');
        }
    } catch (err) {
        showToast('Server connection error', 'error');
    }
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student?')) return;
    try {
        const res = await fetch(`${API_BASE}/students/${id}`, { method: 'DELETE' });
        const json = await res.json();
        if (json.success) {
            showToast('Student deleted successfully', 'success');
            loadStudents();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Failed to delete student', 'error');
    }
}

// 3. Faculty CRUD
async function loadFaculty() {
    try {
        const res = await fetch(`${API_BASE}/faculty`);
        const json = await res.json();
        if (json.success) {
            state.faculty = json.data;
            renderFacultyTable(state.faculty);
        }
    } catch (err) {
        showToast('Error loading faculty directory', 'error');
    }
}

function renderFacultyTable(faculty) {
    const tbody = document.getElementById('facultyTableBody');
    if (!tbody) return;

    if (faculty.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center">No faculty records found.</td></tr>`;
        return;
    }

    tbody.innerHTML = faculty.map(f => `
        <tr>
            <td><strong>${f.facultyId}</strong></td>
            <td>${f.name}</td>
            <td>${f.department}</td>
            <td>${f.email}</td>
            <td>${f.phone}</td>
            <td><span class="badge">${f.designation || 'Faculty'}</span></td>
            <td>
                <div class="table-actions">
                    <button class="btn-action" title="Edit" onclick="editFaculty(${f.id})"><i class="fa-solid fa-pen"></i></button>
                    ${state.currentRole === 'ADMIN' ? `<button class="btn-action delete" title="Delete" onclick="deleteFaculty(${f.id})"><i class="fa-solid fa-trash"></i></button>` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

function filterFaculty() {
    const query = document.getElementById('facultySearchInput')?.value.toLowerCase().trim() || '';
    const dept = document.getElementById('facultyDeptFilter')?.value || '';

    const filtered = state.faculty.filter(f => {
        const matchesQuery = f.name.toLowerCase().includes(query) || f.facultyId.toLowerCase().includes(query);
        const matchesDept = !dept || f.department === dept;
        return matchesQuery && matchesDept;
    });

    renderFacultyTable(filtered);
}

function openFacultyModal(faculty = null) {
    document.getElementById('facultyModalTitle').textContent = faculty ? 'Edit Faculty' : 'Add Faculty Member';
    document.getElementById('facultyIdField').value = faculty ? faculty.id : '';
    document.getElementById('facCode').value = faculty ? faculty.facultyId : `FAC${Date.now().toString().slice(-6)}`;
    document.getElementById('facName').value = faculty ? faculty.name : '';
    document.getElementById('facEmail').value = faculty ? faculty.email : '';
    document.getElementById('facPhone').value = faculty ? faculty.phone : '';
    document.getElementById('facDept').value = faculty ? faculty.department : 'Computer Science & Engineering';
    document.getElementById('facDesignation').value = faculty ? faculty.designation || '' : '';

    showModal('modal-faculty');
}

function editFaculty(id) {
    const faculty = state.faculty.find(f => f.id === id);
    if (faculty) openFacultyModal(faculty);
}

async function handleFacultySubmit(e) {
    e.preventDefault();
    const id = document.getElementById('facultyIdField').value;
    const payload = {
        facultyId: document.getElementById('facCode').value,
        name: document.getElementById('facName').value,
        email: document.getElementById('facEmail').value,
        phone: document.getElementById('facPhone').value,
        department: document.getElementById('facDept').value,
        designation: document.getElementById('facDesignation').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/faculty/${id}` : `${API_BASE}/faculty`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.success) {
            showToast(json.message, 'success');
            closeModal('modal-faculty');
            loadFaculty();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Error saving faculty member', 'error');
    }
}

async function deleteFaculty(id) {
    if (!confirm('Are you sure you want to delete this faculty member?')) return;
    try {
        const res = await fetch(`${API_BASE}/faculty/${id}`, { method: 'DELETE' });
        const json = await res.json();
        if (json.success) {
            showToast('Faculty deleted successfully', 'success');
            loadFaculty();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Failed to delete faculty member', 'error');
    }
}

// 4. Courses CRUD
async function loadCourses() {
    try {
        const res = await fetch(`${API_BASE}/courses`);
        const json = await res.json();
        if (json.success) {
            state.courses = json.data;
            renderCoursesTable(state.courses);
            populateFacultySelect();
        }
    } catch (err) {
        showToast('Error loading courses', 'error');
    }
}

function renderCoursesTable(courses) {
    const tbody = document.getElementById('courseTableBody');
    if (!tbody) return;

    tbody.innerHTML = courses.map(c => `
        <tr>
            <td><strong>${c.courseId}</strong></td>
            <td>${c.courseName}</td>
            <td><code>${c.courseCode}</code></td>
            <td>${c.department}</td>
            <td>${c.semester}</td>
            <td><span class="badge">${c.credits} Credits</span></td>
            <td>${c.facultyName || '<span class="text-muted">Unassigned</span>'}</td>
            <td>
                <div class="table-actions">
                    <button class="btn-action" title="Edit" onclick="editCourse(${c.id})"><i class="fa-solid fa-pen"></i></button>
                    ${state.currentRole === 'ADMIN' ? `<button class="btn-action delete" title="Delete" onclick="deleteCourse(${c.id})"><i class="fa-solid fa-trash"></i></button>` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

async function populateFacultySelect() {
    if (state.faculty.length === 0) await loadFaculty();
    const select = document.getElementById('crsFaculty');
    if (!select) return;

    select.innerHTML = `<option value="">Assign Later</option>` + state.faculty.map(f => `
        <option value="${f.id}">${f.name} (${f.department})</option>
    `).join('');
}

function openCourseModal(course = null) {
    document.getElementById('courseModalTitle').textContent = course ? 'Edit Course' : 'Create Course';
    document.getElementById('courseIdField').value = course ? course.id : '';
    document.getElementById('crsCodeId').value = course ? course.courseId : `CRS${Date.now().toString().slice(-4)}`;
    document.getElementById('crsSubCode').value = course ? course.courseCode : `CS${Date.now().toString().slice(-3)}`;
    document.getElementById('crsName').value = course ? course.courseName : '';
    document.getElementById('crsDept').value = course ? course.department : 'Computer Science & Engineering';
    document.getElementById('crsSem').value = course ? course.semester : 'Semester 5';
    document.getElementById('crsCredits').value = course ? course.credits : 4;
    document.getElementById('crsFaculty').value = course && course.facultyId ? course.facultyId : '';

    showModal('modal-course');
}

function editCourse(id) {
    const course = state.courses.find(c => c.id === id);
    if (course) openCourseModal(course);
}

async function handleCourseSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('courseIdField').value;
    const facultyVal = document.getElementById('crsFaculty').value;

    const payload = {
        courseId: document.getElementById('crsCodeId').value,
        courseCode: document.getElementById('crsSubCode').value,
        courseName: document.getElementById('crsName').value,
        department: document.getElementById('crsDept').value,
        semester: document.getElementById('crsSem').value,
        credits: parseInt(document.getElementById('crsCredits').value),
        facultyId: facultyVal ? parseInt(facultyVal) : null
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/courses/${id}` : `${API_BASE}/courses`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.success) {
            showToast(json.message, 'success');
            closeModal('modal-course');
            loadCourses();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Error saving course', 'error');
    }
}

async function deleteCourse(id) {
    if (!confirm('Are you sure you want to delete this course?')) return;
    try {
        const res = await fetch(`${API_BASE}/courses/${id}`, { method: 'DELETE' });
        const json = await res.json();
        if (json.success) {
            showToast('Course deleted successfully', 'success');
            loadCourses();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Failed to delete course', 'error');
    }
}

// 5. Attendance CRUD & Batch
async function loadAttendanceRecords() {
    try {
        const courseId = document.getElementById('attendanceCourseFilter')?.value || '';
        const date = document.getElementById('attendanceDateFilter')?.value || '';

        let url = `${API_BASE}/attendance`;
        const params = new URLSearchParams();
        if (courseId) params.append('courseId', courseId);
        if (date) params.append('date', date);
        if (params.toString()) url += `?${params.toString()}`;

        const res = await fetch(url);
        const json = await res.json();
        if (json.success) {
            state.attendance = json.data;
            renderAttendanceTable(state.attendance);
            populateCourseFilters();
        }
    } catch (err) {
        showToast('Error loading attendance logs', 'error');
    }
}

function renderAttendanceTable(records) {
    const tbody = document.getElementById('attendanceTableBody');
    if (!tbody) return;

    if (records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center">No attendance records found.</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(r => `
        <tr>
            <td>${r.attendanceDate}</td>
            <td><strong>${r.studentCode}</strong></td>
            <td>${r.studentName}</td>
            <td>${r.courseName}</td>
            <td>
                <span class="badge ${r.status === 'PRESENT' ? 'badge-success' : r.status === 'ABSENT' ? 'badge-danger' : 'badge-warning'}">
                    ${r.status}
                </span>
            </td>
            <td>${r.remarks || '-'}</td>
            <td>
                <div class="table-actions">
                    <button class="btn-action delete" title="Delete" onclick="deleteAttendance(${r.id})"><i class="fa-solid fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

async function populateCourseFilters() {
    if (state.courses.length === 0) await loadCourses();
    const selectFilter = document.getElementById('attendanceCourseFilter');
    const batchSelect = document.getElementById('batchCourseSelect');

    const optionsHTML = `<option value="">Select Course</option>` + state.courses.map(c => `
        <option value="${c.id}">${c.courseName} (${c.courseCode})</option>
    `).join('');

    if (selectFilter) selectFilter.innerHTML = optionsHTML;
    if (batchSelect) batchSelect.innerHTML = optionsHTML;
}

function openBatchAttendanceModal() {
    populateCourseFilters();
    document.getElementById('batchAttendanceDate').valueAsDate = new Date();
    showModal('modal-batch-attendance');
}

async function loadStudentsForAttendance() {
    const courseId = document.getElementById('batchCourseSelect').value;
    const tbody = document.getElementById('batchStudentTableBody');
    if (!courseId) {
        tbody.innerHTML = `<tr><td colspan="4" class="text-center">Please select a course</td></tr>`;
        return;
    }

    if (state.students.length === 0) await loadStudents();

    tbody.innerHTML = state.students.map(s => `
        <tr>
            <td><strong>${s.studentId}</strong></td>
            <td>${s.name}</td>
            <td>
                <select name="status_${s.id}" class="form-select status-select">
                    <option value="PRESENT" selected>PRESENT</option>
                    <option value="ABSENT">ABSENT</option>
                    <option value="LATE">LATE</option>
                </select>
            </td>
            <td><input type="text" name="remarks_${s.id}" class="form-input" placeholder="Optional remark"></td>
        </tr>
    `).join('');
}

async function handleBatchAttendanceSubmit(e) {
    e.preventDefault();
    const courseId = document.getElementById('batchCourseSelect').value;
    const date = document.getElementById('batchAttendanceDate').value;

    const items = state.students.map(s => {
        const statusSelect = document.querySelector(`select[name="status_${s.id}"]`);
        const remarksInput = document.querySelector(`input[name="remarks_${s.id}"]`);
        return {
            studentId: s.id,
            status: statusSelect ? statusSelect.value : 'PRESENT',
            remarks: remarksInput ? remarksInput.value : ''
        };
    });

    const payload = {
        courseId: parseInt(courseId),
        attendanceDate: date,
        studentAttendanceList: items
    };

    try {
        const res = await fetch(`${API_BASE}/attendance/batch`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.success) {
            showToast('Class attendance submitted successfully', 'success');
            closeModal('modal-batch-attendance');
            loadAttendanceRecords();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Error marking batch attendance', 'error');
    }
}

async function deleteAttendance(id) {
    if (!confirm('Delete this attendance entry?')) return;
    try {
        const res = await fetch(`${API_BASE}/attendance/${id}`, { method: 'DELETE' });
        const json = await res.json();
        if (json.success) {
            showToast('Attendance deleted', 'success');
            loadAttendanceRecords();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Failed to delete attendance', 'error');
    }
}

// 6. Academic Records CRUD
async function loadAcademicRecords() {
    try {
        const res = await fetch(`${API_BASE}/academic-records`);
        const json = await res.json();
        if (json.success) {
            state.academicRecords = json.data;
            renderAcademicTable(state.academicRecords);
        }
    } catch (err) {
        showToast('Error loading academic records', 'error');
    }
}

function renderAcademicTable(records) {
    const tbody = document.getElementById('academicTableBody');
    if (!tbody) return;

    if (records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" class="text-center">No academic records found.</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(r => `
        <tr>
            <td><strong>${r.studentCode}</strong></td>
            <td>${r.studentName}</td>
            <td>${r.courseName}</td>
            <td><span class="badge">${r.examType}</span></td>
            <td>${r.marksObtained} / ${r.maxMarks}</td>
            <td><strong>${r.percentage}%</strong></td>
            <td><span class="badge badge-grade">${r.grade}</span></td>
            <td>${r.remarks || '-'}</td>
            <td>
                <div class="table-actions">
                    <button class="btn-action delete" title="Delete" onclick="deleteAcademicRecord(${r.id})"><i class="fa-solid fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

async function openAcademicModal() {
    if (state.students.length === 0) await loadStudents();
    if (state.courses.length === 0) await loadCourses();

    const studentSelect = document.getElementById('acadStudent');
    const courseSelect = document.getElementById('acadCourse');

    studentSelect.innerHTML = state.students.map(s => `<option value="${s.id}">${s.name} (${s.studentId})</option>`).join('');
    courseSelect.innerHTML = state.courses.map(c => `<option value="${c.id}">${c.courseName} (${c.courseCode})</option>`).join('');

    showModal('modal-academic');
}

async function handleAcademicSubmit(e) {
    e.preventDefault();
    const payload = {
        studentId: parseInt(document.getElementById('acadStudent').value),
        courseId: parseInt(document.getElementById('acadCourse').value),
        examType: document.getElementById('acadExamType').value,
        marksObtained: parseFloat(document.getElementById('acadObtained').value),
        maxMarks: parseFloat(document.getElementById('acadMax').value),
        remarks: document.getElementById('acadRemarks').value
    };

    try {
        const res = await fetch(`${API_BASE}/academic-records`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.success) {
            showToast('Academic score recorded successfully', 'success');
            closeModal('modal-academic');
            loadAcademicRecords();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Error recording score', 'error');
    }
}

async function deleteAcademicRecord(id) {
    if (!confirm('Delete this score record?')) return;
    try {
        const res = await fetch(`${API_BASE}/academic-records/${id}`, { method: 'DELETE' });
        const json = await res.json();
        if (json.success) {
            showToast('Academic record deleted', 'success');
            loadAcademicRecords();
            loadDashboardStats();
        }
    } catch (err) {
        showToast('Failed to delete score record', 'error');
    }
}

// Table helper for recent marks on Dashboard
function renderRecentMarksTable(records) {
    const tbody = document.getElementById('recentMarksBody');
    if (!tbody) return;

    if (records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center">No recent records</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(r => `
        <tr>
            <td><strong>${r.studentName}</strong></td>
            <td>${r.courseName}</td>
            <td><span class="badge">${r.examType}</span></td>
            <td>${r.marksObtained}/${r.maxMarks}</td>
            <td>${r.percentage}%</td>
            <td><span class="badge">${r.grade}</span></td>
        </tr>
    `).join('');
}

// Chart.js Visualizations
function renderCharts(stats) {
    // 1. Attendance Overview Line/Bar Chart
    const attCtx = document.getElementById('attendanceChart')?.getContext('2d');
    if (attCtx) {
        if (state.charts.attendance) state.charts.attendance.destroy();

        state.charts.attendance = new Chart(attCtx, {
            type: 'bar',
            data: {
                labels: ['Present Rate', 'Absent Rate', 'Late Rate'],
                datasets: [{
                    label: 'Attendance Statistics (%)',
                    data: [stats.overallAttendancePercentage, 100 - stats.overallAttendancePercentage - 2, 2],
                    backgroundColor: ['#10b981', '#f43f5e', '#f59e0b'],
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: { y: { beginAtZero: true, max: 100 } }
            }
        });
    }

    // 2. Grade Distribution Doughnut Chart
    const gradeCtx = document.getElementById('gradeChart')?.getContext('2d');
    if (gradeCtx) {
        if (state.charts.grade) state.charts.grade.destroy();

        state.charts.grade = new Chart(gradeCtx, {
            type: 'doughnut',
            data: {
                labels: ['Grade A+', 'Grade A', 'Grade B', 'Grade C'],
                datasets: [{
                    data: [45, 30, 15, 10],
                    backgroundColor: ['#6366f1', '#10b981', '#f59e0b', '#ec4899'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'bottom' } }
            }
        });
    }
}

// Modal Helper Utilities
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.classList.add('active');
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.classList.remove('active');
}

// Toast System
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <i class="fa-solid ${type === 'success' ? 'fa-circle-check' : 'fa-circle-exclamation'}"></i>
        <span>${message}</span>
    `;

    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3500);
}
