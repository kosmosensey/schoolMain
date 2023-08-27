package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentBadRequest extends RuntimeException{
    public StudentBadRequest() {
    }

    public StudentBadRequest(String message) {
        super(message);
    }

    public StudentBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentBadRequest(Throwable cause) {
        super(cause);
    }

    public StudentBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
