package net.thrymr.exception;

import net.thrymr.utils.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ApiResponse error = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", ex.getLocalizedMessage());
        ex.printStackTrace();
        return new ResponseEntity(error, error.getStatus());
    }

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleConstraintExceptions(CustomException ex, WebRequest request) {
        ApiResponse error = new ApiResponse(ex.getHttpStatus(), ex.getErrorMessage());
        return new ResponseEntity(error, error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponse error = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(error, error.getStatus());
    }
}