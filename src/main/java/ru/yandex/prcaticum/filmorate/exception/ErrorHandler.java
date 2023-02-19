package ru.yandex.prcaticum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final RuntimeException e) {
        log.error(String.format("Error: %s", e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        log.error(String.format("Error: %s", e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus
    public ErrorResponse handle(final Throwable e) {
        log.error(String.format("Error: %s", e.getMessage()));
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}

class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}