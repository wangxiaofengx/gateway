package com.zy.gis.filter;

import com.zy.com.zy.es.entity.LogInfo;
import com.zy.com.zy.es.service.LogInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * OGC共享服务路由过滤器
 *
 * @author wangxiaofeng
 * @date 2021/12/16 10:56
 */
@Component
public class OGCShareFilter implements GatewayFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(OGCShareFilter.class);

    @Autowired
    private LogInfoService logInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Route route = exchange.getAttribute(org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        Map<String, Object> metadata = route.getMetadata();
        ServerHttpRequest request = exchange.getRequest();
        log.info(request.getPath().value());
        log.info(request.getId());
        log.info(request.getQueryParams().toSingleValueMap().toString());
        //获取信息写入es
        String token = metadata.get("token").toString();
        Long maxTime = Long.parseLong(metadata.get("maxLimitTime").toString());
        LogInfo logInfo = new LogInfo();
        logInfo.setId(metadata.get("servletId").toString());
        logInfo.setName(metadata.get("servletName").toString());
        logInfo.setUserId(metadata.get("userId").toString());
        logInfo.setUserName(metadata.get("userName").toString());
        logInfo.setUserOrgName(metadata.get("orgName").toString());
        logInfo.setUserOrg(metadata.get("orgCode").toString());
        logInfo.setRootId(metadata.get("rootId").toString());
        logInfo.setRootName(metadata.get("rootName").toString());
        logInfo.setServiceToken(token);
        logInfoService.saveData(logInfo);
        //访问次数限制
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = (Date) metadata.get("endDate");
        Date nowDate = new Date();
        long nowdate = nowDate.getTime();
        long enddate = endDate.getTime();

        long reqCount = logInfoService.getReqCount(token);

        if(nowdate<enddate&&maxTime>reqCount){//没有超时
            return chain.filter(exchange);
        }else{//断开请求
            return null;
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
