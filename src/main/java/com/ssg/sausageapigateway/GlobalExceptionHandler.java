package com.ssg.sausageapigateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.sausageapigateway.exception.ErrorCode;
import com.ssg.sausageapigateway.exception.ErrorResponse;
import com.ssg.sausageapigateway.exception.NotFoundException;
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

        if (ex instanceof NotFoundException) {

            return response.writeWith(Mono.fromSupplier(() -> {

                DataBufferFactory bufferFactory = response.bufferFactory();
                try {
                    ErrorResponse errorResponse = ErrorResponse.error(ErrorCode.NOT_FOUND_HEADER_MBR_ID_EXCEPTION);
                    return bufferFactory.wrap(objectMapper.writeValueAsBytes(errorResponse));
                } catch (Exception e) {
                    return bufferFactory.wrap(new byte[0]);
                }
            }));
        }

        return null;
    }
}
