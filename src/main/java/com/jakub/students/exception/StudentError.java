package com.jakub.students.exception;

public enum StudentError {
    CAN_NOT_ADD_COURSE("Can not add course to student"),
    STUDENT_NOT_FOUND("Student does not exists"),

    MAIL_CONFILCT("Student with this mail exists"),

    STUDENT_INACTIVE("Student is inactive");

    private  String message;

    StudentError(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
