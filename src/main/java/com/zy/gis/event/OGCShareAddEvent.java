package com.zy.gis.event;

import com.zy.gis.bo.OGCShare;
import com.zy.gis.dto.RouteDefinition;
import com.zy.gis.factory.OGCShareGatewayFilterFactory;
import com.zy.gis.listener.RedisMessageListener;
import com.zy.gis.service.DynamicRouteService;
import com.zy.proxy.manager.entity.RouteCommServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OGC共享服务添加事件
 *
 * @author wangxiaofeng
 * @date 2021/12/16 10:56
 */
@Component
public class OGCShareAddEvent implements RedisMessageListener<RouteCommServlet> {

    private static final Logger log = LoggerFactory.getLogger(OGCShareAddEvent.class);

    final DynamicRouteService dynamicRouteService;

    public OGCShareAddEvent(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic("OGC-SHARE-ADD");
    }

    @Override
    public void onMessage(RouteCommServlet message, byte[] pattern) {
        log.info("==========服务映射开始==========");
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(message.getToken());
        routeDefinition.setUri(message.getServletUrl());
        routeDefinition.setPathPatterns(message.getUrl());
        log.info("==========服务地址："+routeDefinition.getUri()+"==========");
        log.info("==========服务映射地址："+routeDefinition.getPathPatterns()+"==========");
        log.info("==========服务token："+routeDefinition.getId()+"==========");

        Map<String, String> rewritePath = new HashMap<>();
//        rewritePath.put(routeDefinition.getPathPatterns(), routeDefinition.getUri());
        rewritePath.put(routeDefinition.getPathPatterns(), URI.create(routeDefinition.getUri()).getPath());
        routeDefinition.setRewritePath(rewritePath);

        List<String> filters = new ArrayList<>();
        filters.add(OGCShareGatewayFilterFactory.getFilterName());
        routeDefinition.setFilters(filters);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("servletId", message.getServletId());
        metadata.put("servletName", message.getServletName());
        metadata.put("userName", message.getUserName());
        metadata.put("userId", message.getUserId());
        metadata.put("beginDate",message.getBeginDate());
        metadata.put("endDate",message.getEndDate());
        metadata.put("maxLimitTime",message.getMaxLimitTime());
        metadata.put("limitTime",message.getLimitTime());
        metadata.put("orgCode",message.getOrgCode());
        metadata.put("orgName",message.getOrgName());
        metadata.put("token",message.getToken());
        metadata.put("rootId",message.getRootId());
        metadata.put("rootName",message.getRootName());
        routeDefinition.setMetadata(metadata);
        log.info("==========申请人账号："+routeDefinition.getMetadata().get("userId")+"==========");
        log.info("==========申请人姓名："+routeDefinition.getMetadata().get("userName")+"==========");
        dynamicRouteService.add(routeDefinition);
        log.info("==========服务映射结束==========");
    }
}
