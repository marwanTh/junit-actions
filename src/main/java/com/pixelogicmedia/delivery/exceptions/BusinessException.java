package com.pixelogicmedia.delivery.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final List<String> errors;

    private BusinessException(final HttpStatus httpStatus, final String message, final Throwable cause, final List<String> errors) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public static BusinessException of(final String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message, null, List.of());
    }

    public static BusinessException of(final String message, final Throwable cause) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message, cause, List.of());
    }

    public static BusinessException of(final String message, final Throwable cause, final List<String> errors) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message, cause, errors);
    }

    public static BusinessException of(final HttpStatus httpStatus, final String message, final Throwable cause) {
        return new BusinessException(httpStatus, message, cause, List.of());
    }

    public static BusinessException of(final HttpStatus httpStatus, final String message, final Throwable cause, final List<String> errors) {
        return new BusinessException(httpStatus, message, cause, errors);
    }

    public static BusinessException of(final HttpStatus httpStatus, final String message) {
        return new BusinessException(httpStatus, message, null, List.of());
    }

    public static BusinessException of(final HttpStatus httpStatus, final String message, final List<String> errors) {
        return new BusinessException(httpStatus, message, null, errors);
    }

    public static BusinessException badRequest(final String message) {
        return of(HttpStatus.BAD_REQUEST, message);
    }

    public static BusinessException badRequest(final String message, final List<String> errors) {
        return of(HttpStatus.BAD_REQUEST, message, errors);
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
