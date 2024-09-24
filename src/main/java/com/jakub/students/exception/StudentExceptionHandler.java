package com.jakub.students.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudentExceptionHandler {

    @ExceptionHandler(value = StudentException.class)
    public ResponseEntity<com.jakub.students.exception.ErrorInfo> handleException(StudentException e){
        if (e.getStudentError().equals(StudentError.MAIL_CONFILCT)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorInfo(e.getStudentError().getMessage()));
        }
        if (e.getStudentError().equals(StudentError.STUDENT_NOT_FOUND)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(e.getStudentError().getMessage()));
        }
        if (e.getStudentError().equals(StudentError.STUDENT_INACTIVE)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorInfo(e.getStudentError().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(e.getStudentError().getMessage()));
    }


}
