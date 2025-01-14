package com.jakub.students.service;

import com.jakub.students.exception.StudentError;
import com.jakub.students.exception.StudentException;
import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Student;
import com.jakub.students.repository.EnrolledCourseRepository;
import com.jakub.students.repository.StudentRepository;
import com.jakub.students.storage.ImageStorageClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ImageStorageClient imageStorageClient;
    private final EnrolledCourseRepository enrolledCourseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, ImageStorageClient imageStorageClient, EnrolledCourseRepository enrolledCourseRepository) {
        this.studentRepository = studentRepository;
        this.imageStorageClient = imageStorageClient;
        this.enrolledCourseRepository = enrolledCourseRepository;
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

    @Override
    public void addCourse(Long id, String courseCode) {
        try {
            Student student = studentRepository.findById(id).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
            student.getEnrolledCourses().add(new EnrolledCourse(courseCode, student));
            studentRepository.save(student);
        } catch (Exception e){
            throw new StudentException(StudentError.CAN_NOT_ADD_COURSE);
        }
    }

    @Override
    public Student updateImageProfile(Long id, String containerName, MultipartFile file) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        try {
            deleteImage(containerName, student.getProfileImageUrl());
            student.setProfileImageUrl(uploadImage(containerName, file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return studentRepository.save(student);
    }

    @Override
    public Student markLectureAsUncompleted(Long studentId, String courseId, String lectureId) {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findByCourseIdAndStudentId(courseId, studentId).orElseThrow(()-> new StudentException(StudentError.ENROLLED_COURSE_NOT_FOUND));
        if (enrolledCourse.getCompletedLecturesId().contains(lectureId)){
            enrolledCourse.getCompletedLecturesId().remove(lectureId);
        }else {
            throw new StudentException(StudentError.CAN_NOT_REMOVE_LECTURE);
        }
        enrolledCourseRepository.save(enrolledCourse);
        return studentRepository.findById(studentId).orElseThrow(()-> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }


    @Override
    public void removeCourse(String email, String courseCode) {
        Student student = studentRepository.findByEmail(email).orElseThrow(()-> new StudentException(StudentError.STUDENT_NOT_FOUND));
        student.getEnrolledCourses().removeIf(enrolledCourse -> enrolledCourse.getCourseId().equals(courseCode));
        studentRepository.save(student);
    }

    @Override
    public Student markLectureAsCompleted(Long studentId, String courseId, String lectureId) {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findByCourseIdAndStudentId(courseId, studentId).orElseThrow(()-> new StudentException(StudentError.ENROLLED_COURSE_NOT_FOUND));
        if (enrolledCourse.getCompletedLecturesId().contains(lectureId)){
            throw new StudentException(StudentError.CAN_NOT_ADD_ENROLLEDCOURSE);
        }else {
            enrolledCourse.getCompletedLecturesId().add(lectureId);
        }
        enrolledCourseRepository.save(enrolledCourse);
        return studentRepository.findById(studentId).orElseThrow(()-> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }



    public String uploadImage(String containerName, MultipartFile file)throws IOException {
        try(InputStream inputStream = file.getInputStream()) {
            return this.imageStorageClient.uploadImage(containerName, file.getOriginalFilename(), inputStream, file.getSize());
        }
    }

    public void deleteImage(String containerName, String url)throws IOException {
        try {
            this.imageStorageClient.deleteImage(containerName, url);
        } catch (Exception e){
            throw new StudentException(StudentError.FAILED_TO_UPLOAD_IMAGE);
        }
    }


}
