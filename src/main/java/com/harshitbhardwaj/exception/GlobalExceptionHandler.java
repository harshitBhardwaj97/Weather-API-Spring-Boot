package com.harshitbhardwaj.exception;

import com.harshitbhardwaj.dto.ErrorDTO;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorDTO buildErrorDTO(String message, HttpStatus status) {
        return ErrorDTO.builder().successStatus(false).httpStatus(status).message(message).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDTO> appExceptionHandler(ApplicationException ex) {
        String message = ex.getMessage();
        HttpStatus httpStatus = ex.getStatus();
        ErrorDTO errorDTO = buildErrorDTO(message, httpStatus);
        return new ResponseEntity<>(errorDTO, httpStatus);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        String message = "Required parameter '" + ex.getParameterName() + "' is missing.";
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        String message = ex.getMessage();
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorDTO> handleHttpClientErrorUnauthorizedException(
            HttpClientErrorException.Unauthorized ex) {
        String message = "Make sure you have configured a valid API_KEY.";
        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorDTO> handleHttpClientErrorBadRequestException(
            HttpClientErrorException.BadRequest ex) {
        String message = ex.getMessage();
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorDTO> handleRequestNotPermittedException(
            RequestNotPermitted ex) {
        String message = ex.getMessage();
        HttpStatus statusCode = HttpStatus.TOO_MANY_REQUESTS;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ErrorDTO> handleRedisConnectionFailureException(
            RedisConnectionFailureException ex) {
        String message = "Make sure REDIS server is up and running, then again make the request.";
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ErrorDTO errorDTO = buildErrorDTO(message, statusCode);
        return new ResponseEntity<>(errorDTO, statusCode);
    }
}
