package com.jakub.students.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrolled_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrolledCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseId;

    @ElementCollection
    @CollectionTable(name = "enrolled_course_lectures", joinColumns = @JoinColumn(name = "enrolled_course_id"))
    private List<String> completedLecturesId = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public EnrolledCourse(String courseId, Student student){
        this.courseId = courseId;
        this.student = student;
    }
}
