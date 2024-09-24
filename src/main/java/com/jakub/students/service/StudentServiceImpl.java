package com.jakub.students.service;

import com.jakub.students.exception.StudentError;
import com.jakub.students.exception.StudentException;
import com.jakub.students.model.Student;
import com.jakub.students.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getStudents(Student.Status status) {
        if (status == null){
            return studentRepository.findAll();
        }
        return studentRepository.findAllByStatus(status);
    }

    @Override
    public Student addStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())){
            throw new StudentException(StudentError.MAIL_CONFILCT);
        }else{

            return studentRepository.save(student);
        }

    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        student.setStatus(Student.Status.INACTIVE);
        studentRepository.save(student);
    }

    @Override
    public Student putStudent(Long id, Student student) {

        return studentRepository.findById(id)
            .map(studentFromDB ->{
                if (studentRepository.existsByEmail(student.getEmail()) && !studentFromDB.getEmail().equals(student.getEmail())){
                    throw new StudentException(StudentError.MAIL_CONFILCT);
                }
                studentFromDB.setFirstName(student.getFirstName());
                studentFromDB.setLastName(student.getLastName());
                studentFromDB.setEmail(student.getEmail());
                studentFromDB.setStatus(student.getStatus());
                return studentRepository.save(studentFromDB);
            }).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }

    @Override
    public Student patchStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(studentFromDB -> {
                    if (!StringUtils.isEmpty(student.getFirstName())) {
                        studentFromDB.setFirstName(student.getFirstName());
                    }
                    if (!StringUtils.isEmpty(student.getLastName())) {
                        studentFromDB.setLastName(student.getLastName());
                    }
                    if (!StringUtils.isEmpty(student.getStatus())){
                        studentFromDB.setStatus(student.getStatus());
                    }
                    return studentRepository.save(studentFromDB);
                }).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }

    @Override
    public Student getStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        if (student.getStatus().equals(Student.Status.ACTIVE)){
            return student;
        }
        else {
            throw new StudentException(StudentError.STUDENT_INACTIVE);
        }
    }

    @Override
    public List<Student> getStudentsByEmail(List<String> list) {
        return studentRepository.findAllByEmailIn(list);
    }


}
