package kr.hhplus.be.server.common.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        if(e.getErrorCode().equals(ErrorCode.CLIENT_ERROR)) log.warn(e.getMessage());
        if(e.getErrorCode().equals(ErrorCode.SERVER_ERROR)) log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.fromException((e.getErrorCode().getHttpStatusCode()) , e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception e){
        log.warn(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.fromException(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e){
        log.warn(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.fromException(HttpStatus.BAD_REQUEST , e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.fromException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
