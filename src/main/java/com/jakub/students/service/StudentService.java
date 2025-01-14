package com.jakub.students.service;

import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Student;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    List<Student> getStudents(Student.Status status);
    Student addStudent(Student student);
    void deleteStudent(Long id);


    Student getStudent(Long id);

    List<Student> getStudentsByEmail(List<String> list);

    void addCourse(Long id, String courseCode);

    Student updateImageProfile(Long id, String containerName, MultipartFile file);


    Student markLectureAsCompleted(Long studentId, String courseId, String lectureId);

    Student markLectureAsUncompleted(Long studentId, String courseId, String lectureId);

    void removeCourse(String email, String courseCode);
}
