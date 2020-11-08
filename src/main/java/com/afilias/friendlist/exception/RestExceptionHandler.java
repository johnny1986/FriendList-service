package com.afilias.friendlist.exception;

import com.afilias.friendlist.utils.Constant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage all the exceptions. This is global exception handling mechanism which is
 * configured for all controllers and endpoint within them.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Fall-back handler – a catch-all type of logic that deals with all other exceptions that don't
     * have specific handlers and the internal exceptions such as ResourceNotFoundException
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.error(ex.getMessage());

        if (ex instanceof ResourceNotFoundException) {
            final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND,
                    ex.getLocalizedMessage(), Constant.ERROR);
            return new ResponseEntity<>(apiErrorResponse, new HttpHeaders(),
                    apiErrorResponse.getStatus());
        }

        final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getLocalizedMessage(), Constant.ERROR);

        return new ResponseEntity<>(apiErrorResponse, new HttpHeaders(), apiErrorResponse.getStatus());
    }

    /**
     * Fall-back handler – a catch-all the runtime exceptions
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleAllRuntimeException(final RuntimeException ex,
                                                            final WebRequest request) {
        logger.error(Constant.RUNTIME_EXCEPTION_MESSAGE, ex);

        final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                Constant.RUNTIME_EXCEPTION_MESSAGE, Constant.ERROR);

        return new ResponseEntity<>(apiErrorResponse, new HttpHeaders(), apiErrorResponse.getStatus());
    }


    /**
     * This exception is thrown when argument annotated with @Valid failed validation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        logger.error(ex.getMessage());

        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(),
                errors);

        return handleExceptionInternal(ex, apiErrorResponse, headers, apiErrorResponse.getStatus(),
                request);
    }

    /**
     * This exception is thrown when request missing parameter
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        logger.error(ex.getMessage());

        final String error = ex.getParameterName() + " parameter is missing";

        final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiErrorResponse, new HttpHeaders(),
                apiErrorResponse.getStatus());
    }

    /**
     * This exception is thrown when method argument is not the expected type.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.error(ex.getMessage());

        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(), error);

        return new ResponseEntity(apiErrorResponse, new HttpHeaders(),
                apiErrorResponse.getStatus());
    }

    /**
     * This exception reports the result of constraint violations.
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            final DataIntegrityViolationException ex,
            final WebRequest request) {
        logger.error(ex.getMessage());

        final List<String> errors = new ArrayList<>();

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(),
                errors);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


}
