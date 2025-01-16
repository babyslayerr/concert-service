package kr.hhplus.be.server.common.exception;

public class CustomException extends RuntimeException{

    // 커스텀 생성시점에 status code 넘기게 만듬
    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode,String message){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
