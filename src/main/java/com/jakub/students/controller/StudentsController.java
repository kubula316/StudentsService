package com.jakub.students.controller;

import com.jakub.students.repository.StudentRepository;
import com.jakub.students.model.Student;
import com.jakub.students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping
    public List<Student> getStudents(@RequestParam(required = false) Student.Status status) {
        return studentService.getStudents(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody @Valid Student student) {
        return studentService.addStudent(student);
    }
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id){
        return studentService.getStudent(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
    @PutMapping("/{id}")
    public Student putStudent(@PathVariable Long id,@RequestBody @Valid Student student){
        return studentService.putStudent(id, student);
    }


    @PatchMapping("/{id}")
    public Student patchStudent(@PathVariable Long id,@RequestBody Student student){
        return studentService.patchStudent(id, student);
    }

    @PostMapping("/members")
    public List<Student> getCourseMembers(@RequestBody List<String> mailList){
        return studentService.getStudentsByEmail(mailList);
    }

}
