package com.zy.gis.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class IndexController {

    @Value("${server.port}")
    Integer port;

    @GetMapping("/")
    public Mono<String> index() {
        return Mono.just("Hello World! " + port);
    }

    @GetMapping("/mapgis")
    public Mono<String> mapGis() {
        return Mono.just("权限服务正常!");
    }
}
