package com.jakub.students.service;

import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Student;
import com.jakub.students.repository.EnrolledCourseRepository;
import com.jakub.students.repository.StudentRepository;
import com.jakub.students.storage.ImageStorageClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ImageStorageClient imageStorageClient;

    @Mock
    private EnrolledCourseRepository enrolledCourseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void shouldAddCourseToStudent() {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .status(Student.Status.ACTIVE)
                .enrolledCourses(new ArrayList<>())
                .build();
        String courseCode = "COURSE123";

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // When
        studentService.addCourse(1L, courseCode);

        // Then
        assertEquals(1, student.getEnrolledCourses().size());
        assertEquals(courseCode, student.getEnrolledCourses().get(0).getCourseId());
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(student);
    }

    @Test
    void shouldMarkLectureAsCompleted() {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .status(Student.Status.ACTIVE)
                .build();
        EnrolledCourse course = new EnrolledCourse("COURSE123", student);
        course.setCompletedLecturesId(new ArrayList<>());

        when(enrolledCourseRepository.findByCourseIdAndStudentId("COURSE123", 1L))
                .thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrolledCourseRepository.save(course)).thenReturn(course);

        // When
        Student result = studentService.markLectureAsCompleted(1L, "COURSE123", "LECTURE1");

        // Then
        assertTrue(course.getCompletedLecturesId().contains("LECTURE1"));
        assertEquals(student, result);
        verify(enrolledCourseRepository).findByCourseIdAndStudentId("COURSE123", 1L);
        verify(enrolledCourseRepository).save(course);
        verify(studentRepository).findById(1L);
    }

    @Test
    void shouldDeactivateStudent() {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .status(Student.Status.ACTIVE)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        // When
        studentService.deleteStudent(1L);

        // Then
        assertEquals(Student.Status.INACTIVE, student.getStatus());
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(student);
    }

    @Test
    void shouldRemoveCourseForStudent() {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .status(Student.Status.ACTIVE)
                .enrolledCourses(new ArrayList<>(List.of(
                        new EnrolledCourse("COURSE123", null),
                        new EnrolledCourse("COURSE456", null)
                )))
                .build();

        when(studentRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(student));

        // When
        studentService.removeCourse("john.doe@example.com", "COURSE123");

        // Then
        assertEquals(1, student.getEnrolledCourses().size());
        assertEquals("COURSE456", student.getEnrolledCourses().get(0).getCourseId());
        verify(studentRepository).findByEmail("john.doe@example.com");
        verify(studentRepository).save(student);
    }

    @Test
    void shouldUpdateImageProfile() throws IOException {
        // Given
        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .profileImageUrl("old-image-url")
                .build();
        MultipartFile file = mock(MultipartFile.class);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(file.getInputStream()).thenReturn(mock(InputStream.class));
        when(file.getOriginalFilename()).thenReturn("new-image.jpg");
        when(imageStorageClient.uploadImage("container", "new-image.jpg", file.getInputStream(), file.getSize()))
                .thenReturn("new-image-url");
        when(studentRepository.save(student)).thenReturn(student);

        // When
        Student result = studentService.updateImageProfile(1L, "container", file);

        // Then
        assertEquals("new-image-url", result.getProfileImageUrl());
        verify(imageStorageClient).uploadImage("container", "new-image.jpg", file.getInputStream(), file.getSize());
        verify(studentRepository).save(student);
    }


}
