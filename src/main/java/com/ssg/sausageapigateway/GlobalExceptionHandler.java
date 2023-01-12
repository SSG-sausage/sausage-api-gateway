package com.ssg.sausageapigateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.sausageapigateway.exception.ErrorCode;
import com.ssg.sausageapigateway.exception.ErrorResponse;
import com.ssg.sausageapigateway.exception.MbrIdHeaderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // MbrIdHeaderNotFoundException 에러 핸들링
        if (ex instanceof MbrIdHeaderNotFoundException) {
            return createErrorResponse(response, ErrorCode.NOT_FOUND_HEADER_MBR_ID_EXCEPTION);
        }

        // 그 외 내부 INTERNAL_SERVER_EXCEPTION 반환
        return createErrorResponse(response, ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }

    private Mono<Void> createErrorResponse(ServerHttpResponse response, ErrorCode errorCode) {
        return response.writeWith(Mono.fromSupplier(() -> {

            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                ErrorResponse errorResponse = ErrorResponse.error(errorCode);
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(errorResponse));
            } catch (Exception e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
