package com.zy.gis.config;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

public class CModifyResponseGatewayFilterFactory extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return new JwtResponseGatewayFilter();
    }

    public class JwtResponseGatewayFilter implements GatewayFilter, Ordered {
        //注意此处一定要比-1小
        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return chain.filter(exchange.mutate().response(decorate(exchange)).build());
        }

        /**
         * 解决netty buffer默认长度1024导致的接受body不全问题
         * @param exchange
         * @return
         */
        @SuppressWarnings("unchecked")
        private ServerHttpResponse decorate(ServerWebExchange exchange) {
            return new ServerHttpResponseDecorator(exchange.getResponse()) {

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                    String originalResponseContentType = exchange
                            .getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE,
                            originalResponseContentType);

                    ClientResponse clientResponse = ClientResponse
                            .create(exchange.getResponse().getStatusCode())
                            .headers(headers -> headers.putAll(httpHeaders))
                            .body(Flux.from(body)).build();

                    //修改body
                    Mono<String> modifiedBody = clientResponse.bodyToMono(String.class)
                            .flatMap(originalBody -> modifyBody()
                                    .apply(exchange,Mono.just(originalBody)));

                    BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                            String.class);
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                            exchange, exchange.getResponse().getHeaders());
                    return bodyInserter.insert(outputMessage, new BodyInserterContext())
                            .then(Mono.defer(() -> {
                                Flux<DataBuffer> messageBody = outputMessage.getBody();
                                HttpHeaders headers = getDelegate().getHeaders();
                                if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                    messageBody = messageBody.doOnNext(data -> headers
                                            .setContentLength(data.readableByteCount()));
                                }
                                return getDelegate().writeWith(messageBody);
                            }));
                }

                /**
                 * 修改body
                 * @return apply 返回Mono<String>，数据是修改后的body
                 */
                private BiFunction<ServerWebExchange,Mono<String>,Mono<String>> modifyBody(){
                    return (exchange,json)-> {
                        AtomicReference<String> jsonObject = new AtomicReference<>();
                        json.subscribe(
                                value -> {
                                    //value即为body
                                },
                                Throwable::printStackTrace
                        );
                        return Mono.just(jsonObject.get());
                    };
                }


                @Override
                public Mono<Void> writeAndFlushWith(
                        Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };
        }
    }
}