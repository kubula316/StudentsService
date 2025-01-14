package com.jakub.students.service;

import com.jakub.students.exception.StudentException;
import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Student;
import com.jakub.students.repository.EnrolledCourseRepository;
import com.jakub.students.repository.StudentRepository;
import com.jakub.students.storage.ImageStorageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrolledCourseRepository enrolledCourseRepository;

    @Mock
    private ImageStorageClient imageStorageClient;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStudents_ShouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(new Student()));

        List<Student> students = studentService.getStudents(null);

        assertEquals(1, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void addStudent_ShouldSaveNewStudent() {
        Student student = new Student();
        student.setEmail("test@example.com");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.addStudent(student);

        assertEquals(student, result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void addStudent_ShouldThrowExceptionIfEmailExists() {
        Student student = new Student();
        student.setEmail("test@example.com");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        assertThrows(StudentException.class, () -> studentService.addStudent(student));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_ShouldSetStatusInactive() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        assertEquals(Student.Status.INACTIVE, student.getStatus());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void getStudent_ShouldReturnActiveStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setStatus(Student.Status.ACTIVE);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudent(1L);

        assertEquals(student, result);
    }



    @Test
    void addCourse_ShouldAddEnrolledCourse() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.addCourse(1L, "CS101");

        assertEquals(1, student.getEnrolledCourses().size());
        EnrolledCourse course = student.getEnrolledCourses().get(0);
        assertEquals("CS101", course.getCourseId());
        assertEquals(student, course.getStudent());
        verify(studentRepository, times(1)).save(student);
    }




    @Test
    void markLectureAsUncompleted_ShouldThrowExceptionIfLectureNotFound() {
        EnrolledCourse course = new EnrolledCourse();
        course.setCompletedLecturesId(new ArrayList<>());
        course.setCourseId("course1");
        course.setId(1L);

        when(enrolledCourseRepository.findByCourseIdAndStudentId("course1", 1L))
                .thenReturn(Optional.of(course));

        assertThrows(StudentException.class, () -> {
            studentService.markLectureAsUncompleted(1L, "course1", "lecture1");
        });

        verify(enrolledCourseRepository, never()).save(course);
    }



    @Test
    void markLectureAsCompleted_ShouldAddLectureId() {
        EnrolledCourse course = new EnrolledCourse();
        course.setCompletedLecturesId(new ArrayList<>());
        course.setCourseId("course1");
        course.setId(1L);

        Student student = new Student();
        student.setId(1L);
        course.setStudent(student);

        when(enrolledCourseRepository.findByCourseIdAndStudentId("course1", 1L))
                .thenReturn(Optional.of(course));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.markLectureAsCompleted(1L, "course1", "lecture1");

        assertTrue(course.getCompletedLecturesId().contains("lecture1"));
        verify(enrolledCourseRepository, times(1)).save(course);
    }

    @Test
    void markLectureAsUncompleted_ShouldRemoveLectureId() {
        EnrolledCourse course = new EnrolledCourse();
        course.setCompletedLecturesId(new ArrayList<>(List.of("lecture1")));
        course.setCourseId("course1");
        course.setId(1L);

        Student student = new Student();
        student.setId(1L);
        course.setStudent(student);

        when(enrolledCourseRepository.findByCourseIdAndStudentId("course1", 1L))
                .thenReturn(Optional.of(course));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.markLectureAsUncompleted(1L, "course1", "lecture1");

        assertFalse(course.getCompletedLecturesId().contains("lecture1"));
        verify(enrolledCourseRepository, times(1)).save(course);
    }

    @Test
    void removeCourse_ShouldRemoveEnrolledCourse() {
        // Given
        Student student = new Student();
        student.setId(1L);
        EnrolledCourse course = new EnrolledCourse();
        course.setCourseId("CS101");
        course.setStudent(student);
        student.setEnrolledCourses(new ArrayList<>(List.of(course)));

        when(studentRepository.findByEmail("email@example.com"))
                .thenReturn(Optional.of(student));

        studentService.removeCourse("email@example.com", "CS101");

        assertTrue(student.getEnrolledCourses().isEmpty());
        verify(studentRepository, times(1)).save(student);
    }
}
