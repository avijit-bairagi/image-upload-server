package com.ovi.EnkaizenTest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Set<String> messages = new HashSet<>();

        ex.getBindingResult().getAllErrors().forEach(error -> messages.add(error.getDefaultMessage()));

        return getResponseEntity(new ArrayList<>(messages));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return getResponseEntity(Collections.singletonList("Path variable required."));

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {

        return getResponseEntity(Collections.singletonList("No handler found."));

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = "Path variable or param value type mismatch.";

        if (ex instanceof MethodArgumentTypeMismatchException) {

            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            message = "Path variable or param value type mismatch for " + exception.getName();
        }

        return getResponseEntity(Collections.singletonList(message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return getResponseEntity(Collections.singletonList(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        return getResponseEntity(Collections.singletonList(ex.getMessage()));
    }


    private ResponseEntity<Object> getResponseEntity(List<String> messages) {

        ResponseBody<Object> responseBody = new ResponseBody<>();
        responseBody.setCode(ResponseStatus.UNKNOWN.getCode());
        responseBody.setMessage(String.join(",", messages));

        logger.error("Error occurred while serving the request. Response: {}", responseBody);

        return ResponseEntity.ok(responseBody);
    }
}