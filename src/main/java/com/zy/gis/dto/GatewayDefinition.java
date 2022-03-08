package com.zy.gis.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网关配置
 *
 * @Author wangxiaofeng
 * @Date 2020/09/30 11:25
 */
@Component
@ConfigurationProperties(prefix = "gateway")
public class GatewayDefinition {

    /**
     * 路由列表
     */
    List<RouteDefinition> routes;

    public List<RouteDefinition> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteDefinition> routes) {
        this.routes = routes;
    }
}
