package com.jakub.students.service;

import com.jakub.students.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getStudents(Student.Status status);
    Student addStudent(Student student);
    void deleteStudent(Long id);

    Student putStudent(Long id, Student student);

    Student patchStudent(Long id, Student student);

    Student getStudent(Long id);

    List<Student> getStudentsByEmail(List<String> list);

}
