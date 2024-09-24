package com.jakub.students.exception;

public enum StudentError {
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
