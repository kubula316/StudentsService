package com.jakub.students.repository;

import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long> {
    Optional<EnrolledCourse> findByCourseIdAndStudentId(String courseId, Long studentId);
}
