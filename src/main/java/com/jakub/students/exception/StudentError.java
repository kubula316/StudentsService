package com.jakub.students.exception;

public enum StudentError {
    CAN_NOT_ADD_COURSE("Can not add course to student"),
    STUDENT_NOT_FOUND("Student does not exists"),

    MAIL_CONFILCT("Student with this mail exists"),

    STUDENT_INACTIVE("Student is inactive"),
    FAILED_TO_UPLOAD_IMAGE("Failed to upload image"),
    ENROLLED_COURSE_NOT_FOUND("Cannot find enrolledCourse"),
    CAN_NOT_ADD_ENROLLEDCOURSE("Course arleady have lecture marked"),
    CAN_NOT_REMOVE_LECTURE("Can't remove lecture from marked");

    private  String message;

    StudentError(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
