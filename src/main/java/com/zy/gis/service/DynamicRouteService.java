package com.zy.gis.service;

import com.zy.gis.dto.RouteDefinition;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态路由服务
 *
 * @author wangxiaofeng
 * @date 2021/12/16 9:18
 */
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    final
    RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    final
    RouteLocatorBuilder routeLocatorBuilder;

    public DynamicRouteService(RouteDefinitionWriter routeDefinitionWriter, RouteLocatorBuilder routeLocatorBuilder) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeLocatorBuilder = routeLocatorBuilder;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void add(RouteDefinition routeDefinition) {
        org.springframework.cloud.gateway.route.RouteDefinition routeDefinition1 = new org.springframework.cloud.gateway.route.RouteDefinition();
        routeDefinition1.setId(routeDefinition.getId());
        routeDefinition1.setUri(URI.create(routeDefinition.getUri()));
        List<PredicateDefinition> prediscates = new ArrayList<>();
        prediscates.add(new PredicateDefinition("Path=" + routeDefinition.getPathPatterns()));
        routeDefinition1.setPredicates(prediscates);
        Map<String, String> rewritePath = routeDefinition.getRewritePath();
        if (rewritePath != null && rewritePath.size() > 0) {
            List<FilterDefinition> filters = new ArrayList<>();
            List<String> customFilters = routeDefinition.getFilters();
            rewritePath.forEach((k, v) -> {
                filters.add(new FilterDefinition("RewritePath=" + k + "," + v));
            });
            customFilters.forEach(filter -> filters.add(new FilterDefinition(filter + "=path," + routeDefinition.getUri())));
            routeDefinition1.setFilters(filters);
        }
        routeDefinition1.setMetadata(routeDefinition.getMetadata());
        routeDefinitionWriter.save(Mono.just(routeDefinition1)).subscribe();

        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void delete(RouteDefinition routeDefinition) {
        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
