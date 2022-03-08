package com.zy.gis.filter;

import com.zy.common.sys.entity.SysRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PermissFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(PermissFilter.class);

    public static final Map<String, Set<String>> permissMap = new HashMap<>(64);

    @Autowired
    ReactiveRedisTemplate reactiveRedisTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${gateway.inexclude:}")
    String inExclude;

    @Value("${gateway.premissInterfaceAddress:}")
    String premissInterfaceAddress;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        String url = exchange.getRequest().getURI().getPath();
        log.debug("url : {}", url);
        log.debug("params : {}", params);
        // 设置不过滤地址
        if ((!StringUtils.isEmpty(inExclude)) && url.matches(inExclude)) {
            return chain.filter(exchange);
        }

        String token = params.getFirst("sessionId");
        String layerId = params.getFirst("id");

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(layerId)) {
            return forbidden(exchange);
        }

        Object roleObj = redisTemplate.boundHashOps("session:" + token).get("role");
        if (roleObj == null || !(roleObj instanceof List)) {
            return forbidden(exchange);
        }
        List<SysRole> roleList = (List<SysRole>) roleObj;
        List<String> roleIdList = roleList.stream().map(sysRole -> sysRole.getOrgCode() + "_" + sysRole.getRoleId()).collect(Collectors.toList());

        for (String roleId : roleIdList) {
            if (!permissMap.containsKey(roleId)) {
                Set<String> layerIdSet = new HashSet<>(64);
                Map result = restTemplate.getForObject(premissInterfaceAddress + roleId, Map.class);
                if (result.get("success").toString().equals("true")) {
                    permissMap.put(roleId, layerIdSet);
                    List<Map> data = (List<Map>) result.get("data");
                    data.forEach(map -> {
                        Map layerInfo = (Map) map.get("layerInfo");
                        if (layerInfo != null) {
                            layerIdSet.add(String.valueOf(layerInfo.get("id")));
                        }
                    });
                }
            }
            if (permissMap.get(roleId).contains(layerId)) {
                return chain.filter(exchange);
            }
        }
        return forbidden(exchange);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
//        DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
//        DataBuffer defaultDataBuffer = defaultDataBufferFactory.wrap("没有权限".getBytes());
//        exchange.getResponse().writeWith(Mono.just(defaultDataBuffer));
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
