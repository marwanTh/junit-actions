package com.pixelogicmedia.delivery.api;


import com.fasterxml.jackson.core.JacksonException;
import com.pixelogicmedia.delivery.api.v1.models.ConstraintViolationResource;
import com.pixelogicmedia.delivery.api.v1.models.ErrorResource;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import cz.jirutka.rsql.parser.RSQLParserException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RSQLParserException.class})
    protected ResponseEntity<ErrorResource> handleRSQLException(final RSQLParserException e) {
        return ResponseEntity.badRequest().body(new ErrorResource().message(e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResource> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new ErrorResource().message(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResource> handleConstraintViolationException(final ConstraintViolationException e) {
        final var error = new ErrorResource().message(e.getMessage());
        error.constraintViolations(e.getConstraintViolations()
                .stream().map(v -> new ConstraintViolationResource().message(v.getMessage()).property(v.getPropertyPath().toString())).toList());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResource> handleBusinessException(final BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResource().message(e.getMessage())
                .errorMessages(e.getErrors()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResource> handleNoSuchElementException(final NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResource().message(e.getMessage()));
    }

    @ExceptionHandler(JacksonException.class)
    protected ResponseEntity<ErrorResource> handleJacksonException(final JacksonException e) {
        return ResponseEntity.badRequest().body(new ErrorResource().message(e.getMessage()));
    }

    @ExceptionHandler(UncheckedIOException.class)
    protected ResponseEntity<ErrorResource> handleUncheckedIOException(final UncheckedIOException e) {
        if (e.getCause() instanceof JacksonException je) {
            return handleJacksonException(je);
        }

        return ResponseEntity.internalServerError().body(new ErrorResource().message(e.getCause().getMessage()));
    }

    @ExceptionHandler(EnumMappingException.class)
    protected ResponseEntity<ErrorResource> handleEnumMappingException(final EnumMappingException e) {
        return ResponseEntity.badRequest().body(new ErrorResource().message(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        var error = new ErrorResource().message(ex.getMessage());

        error.constraintViolations(ex.getAllErrors()
                .stream()
                .map(e -> new ConstraintViolationResource()
                        .property(argumentNames(e.getArguments()))
                        .message(e.getDefaultMessage()))
                .toList());

        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorResource().message(ex.getMessage()));
    }

    private String argumentNames(Object[] arguments) {
        if (arguments == null) {
            return null;
        }

        return Arrays.stream(arguments)
                .filter(DefaultMessageSourceResolvable.class::isInstance)
                .map(DefaultMessageSourceResolvable.class::cast)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
    }
}
