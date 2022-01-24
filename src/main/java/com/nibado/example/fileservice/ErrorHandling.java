package com.nibado.example.fileservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandling {
    @ExceptionHandler(FileController.FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Problem handleException(final FileController.FileNotFoundException ex) {
        return new Problem("file_not_found", "The file was not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(FileController.BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Problem handleException(final FileController.BadRequestException ex) {
        return new Problem("bad_request", "Bad request", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    //Catch-all
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Problem handleException(final Throwable ex) {
        return new Problem("internal_server_error", "An unexpected error occured", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    //RFC7807 Problem response https://datatracker.ietf.org/doc/html/rfc7807
    public record Problem(String type, String title, String detail, int status){}
}
