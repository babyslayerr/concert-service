package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    CLIENT_ERROR(400),
    SERVER_ERROR(500);

    private final int statusCode;

    ErrorCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return HttpStatus.valueOf(this.statusCode);

    }
}
