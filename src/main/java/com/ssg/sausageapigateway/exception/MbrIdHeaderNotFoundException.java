package com.ssg.sausageapigateway.exception;

public class MbrIdHeaderNotFoundException extends NotFoundException {

    public MbrIdHeaderNotFoundException() {
        super("Request Header에 ID값이 존재하지 않습니다.", ErrorCode.NOT_FOUND_HEADER_MBR_ID_EXCEPTION);
    }
}
