package com.ssg.sausageapigateway;

import com.ssg.sausageapigateway.exception.ErrorCode;
import com.ssg.sausageapigateway.exception.NotFoundException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthFilter extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (request.getHeaders().getFirst("mbrId") == null) {
                throw new NotFoundException("Request Header에 ID값이 존재하지 않습니다.",
                        ErrorCode.NOT_FOUND_HEADER_MBR_ID_EXCEPTION);
            }

            return chain.filter(exchange);
        });
    }


}
