package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FacultyBadRequest extends RuntimeException{
    public FacultyBadRequest() {
    }

    public FacultyBadRequest(String message) {
        super(message);
    }

    public FacultyBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public FacultyBadRequest(Throwable cause) {
        super(cause);
    }

    public FacultyBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
