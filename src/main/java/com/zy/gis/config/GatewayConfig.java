package com.zy.gis.config;

import com.zy.gis.dto.GatewayDefinition;
import com.zy.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    @Autowired
    GatewayDefinition gatewayDefinition;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {

        RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes();
        if (gatewayDefinition == null || gatewayDefinition.getRoutes() == null) {
            return builder.build();
        }
        gatewayDefinition.getRoutes().forEach(routeDefinition -> {
            builder.route(r -> {
                BooleanSpec booleanSpec = r.path(routeDefinition.getPathPatterns());
                booleanSpec.filters(f -> {
                    Map<String, String> requestParameter = routeDefinition.getRequestParameter();
                    if (requestParameter != null && !requestParameter.isEmpty()) {
                        requestParameter.forEach((k, v) -> f.addRequestParameter(k, v));
                    }
                    Map<String, String> rewritePath = routeDefinition.getRewritePath();
                    if (rewritePath != null && !rewritePath.isEmpty()) {
                        rewritePath.forEach((k, v) -> f.rewritePath(k, v));
                    }

                    List<String> filters = routeDefinition.getFilters();
                    if (filters != null && !filters.isEmpty()) {
                        filters.forEach(className -> {
                            try {
                                Class<GatewayFilter> clazz = (Class<GatewayFilter>) Class.forName(className);
                                GatewayFilter gatewayFilter = SpringContext.getBean(clazz);
                                if (gatewayFilter == null) {
                                    gatewayFilter = clazz.getDeclaredConstructor().newInstance();
                                }
                                f.filter(gatewayFilter);
                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    return f;
                });
                return booleanSpec.uri(routeDefinition.getUri());
            });
        });

        return builder.build();
    }
}
