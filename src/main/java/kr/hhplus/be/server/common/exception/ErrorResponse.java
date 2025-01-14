package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
         Integer code,
         String message
) {
    

    public static ErrorResponse fromException(HttpStatus httpStatus, String message) {
        // httpCode 및 message 반환
        return new ErrorResponse(httpStatus.value(), message);
    }
}
