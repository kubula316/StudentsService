package com.jakub.students.repository;

import com.jakub.students.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
//    List<Student> findByLastName(String lastName, Pageable pageable);
//    List<Student> findByLastNameAndFirstNameIsNotLikeAllIgnoreCase(String lastName, String firstName);
//
//    @Query("Select s from Student s where s.firstName='Marian'")
//    List<Student> findStudentsWithNameMarian();
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);

    List<Student> findAllByEmailIn(List<String> email);

    List<Student> findAllByStatus(Student.Status status);
}



