package com.zy.gis.listener;

import com.zy.gis.service.DynamicRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * OGC共享服务初始化
 *
 * @author wangxiaofeng
 * @date 2021/12/16 9:01
 */
@Component
public class OGCShareInitialize implements ApplicationListener<ApplicationStartedEvent> {

    private static final String ROUTE_PREFIX = "/ogc/share";

    private static final Logger log = LoggerFactory.getLogger(OGCShareInitialize.class);

    final DynamicRouteService dynamicRouteService;

    final RestTemplate restTemplate;

    public OGCShareInitialize(DynamicRouteService dynamicRouteService, RestTemplate restTemplate) {
        this.dynamicRouteService = dynamicRouteService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        log.info("加载OGC共享服务配置");
    }
}
