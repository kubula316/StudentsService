package com.jakub.students.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseProgress {
    private String courseId;
    
    private List<Long> completedLessons = new ArrayList<>();

}
