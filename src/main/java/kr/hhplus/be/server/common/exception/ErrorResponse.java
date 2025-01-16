package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record ErrorResponse(
         Integer code,
         String message
) {
    

    public static ErrorResponse fromException(HttpStatusCode httpStatusCode, String message) {
        // httpCode 및 message 반환
        return new ErrorResponse(httpStatusCode.value(), message);
    }
}
