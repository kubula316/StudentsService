package com.jakub.students.controller;


import com.jakub.students.controller.StudentsController;
import com.jakub.students.model.Student;
import com.jakub.students.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentsControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentsController studentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

        when(studentService.addStudent(student)).thenReturn(student);

        Student result = studentsController.addStudent(student);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(studentService, times(1)).addStudent(student);
    }

    @Test
    void testGetStudent() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudent(1L)).thenReturn(student);

        Student result = studentsController.getStudent(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentService, times(1)).getStudent(1L);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent(1L);

        assertDoesNotThrow(() -> studentsController.deleteStudent(1L));

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void testGetStudents() {
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getStudents(null)).thenReturn(students);

        List<Student> result = studentsController.getStudents(null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentService, times(1)).getStudents(null);
    }

    @Test
    void testMarkLectureAsCompleted() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.markLectureAsCompleted(1L, "course123", "lecture123")).thenReturn(student);

        Student result = studentsController.markLectureAsCompleted(1L, "course123", "lecture123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentService, times(1)).markLectureAsCompleted(1L, "course123", "lecture123");
    }

    @Test
    void testMarkLectureAsUncompleted() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.markLectureAsUncompleted(1L, "course123", "lecture123")).thenReturn(student);

        Student result = studentsController.markLectureAsUncompleted(1L, "course123", "lecture123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentService, times(1)).markLectureAsUncompleted(1L, "course123", "lecture123");
    }

    @Test
    void testAddCourse() {
        doNothing().when(studentService).addCourse(1L, "course123");

        assertDoesNotThrow(() -> studentsController.addCourse(1L, "course123"));

        verify(studentService, times(1)).addCourse(1L, "course123");
    }

    @Test
    void testRemoveCourse() {
        doNothing().when(studentService).removeCourse("email@example.com", "course123");

        assertDoesNotThrow(() -> studentsController.removeCourse("email@example.com", "course123"));

        verify(studentService, times(1)).removeCourse("email@example.com", "course123");
    }

    @Test
    void testGetCourseMembers() {
        Student student = new Student();
        student.setId(1L);

        List<String> emailList = Arrays.asList("email1@example.com", "email2@example.com");
        List<Student> students = List.of(student);

        when(studentService.getStudentsByEmail(emailList)).thenReturn(students);

        List<Student> result = studentsController.getCourseMembers(emailList);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(studentService, times(1)).getStudentsByEmail(emailList);
    }

    @Test
    void testUpdateImage() {
        Student student = new Student();
        student.setId(1L);

        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.png", "image/png", "some-image-data".getBytes());

        when(studentService.updateImageProfile(1L, "container", mockFile)).thenReturn(student);

        Student result = studentsController.updateImage(1L, "container", mockFile);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentService, times(1)).updateImageProfile(1L, "container", mockFile);
    }
}
