package com.ssg.sausageapigateway.filter;

import com.ssg.sausageapigateway.exception.MbrIdHeaderNotFoundException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class MbrAuthFilter extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (request.getHeaders().getFirst("mbrId") == null) {
                throw new MbrIdHeaderNotFoundException();
            }

            return chain.filter(exchange);
        });
    }


}
