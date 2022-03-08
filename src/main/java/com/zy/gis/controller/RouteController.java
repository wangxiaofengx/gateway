package com.zy.gis.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.javafaker.Faker;
import com.zy.com.zy.es.entity.LogInfo;
import com.zy.com.zy.es.service.LogInfoService;
import com.zy.gis.dto.RouteDefinition;
import com.zy.gis.factory.OGCShareGatewayFilterFactory;
import com.zy.gis.service.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * 路由控制器
 *
 * @author wangxiaofeng
 * @date 2021/12/15 9:31
 */
@RestController
@RequestMapping("/route")
@Slf4j
public class RouteController {


    final LogInfoService logInfoService;

    final DynamicRouteService dynamicRouteService;

    public RouteController(LogInfoService logInfoService, DynamicRouteService dynamicRouteService) {
        this.logInfoService = logInfoService;
        this.dynamicRouteService = dynamicRouteService;
    }

    @GetMapping("/addAll")
    public Mono<String> addAll(){
        String strurl = "http://172.16.103.200:8600/proxy/OGC/getAllServlet";
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(strurl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer();
            while((line = reader.readLine())!=null){
                buffer.append(line);
            }
            reader.close();
            httpURLConnection.disconnect();
            log.info("buffer:"+buffer);
            String result = buffer.toString();
            JSONArray jsonArray = new JSONArray(buffer.toString());
            //将数据重新发布
            for(int i =0;i<jsonArray.size();i++){
                RouteDefinition routeDefinition = new RouteDefinition();
                JSONObject obj = jsonArray.getJSONObject(i);
                routeDefinition.setId(obj.get("token").toString());
                routeDefinition.setUri(obj.get("servletUrl").toString());
                routeDefinition.setPathPatterns(obj.get("url").toString());

                Map<String, String> rewritePath = new HashMap<>();
                rewritePath.put(routeDefinition.getPathPatterns(), URI.create(routeDefinition.getUri()).getPath());
                routeDefinition.setRewritePath(rewritePath);

                List<String> filters = new ArrayList<>();
                filters.add(OGCShareGatewayFilterFactory.getFilterName());
                routeDefinition.setFilters(filters);

                Map<String, Object> metadata = new HashMap<>();
                metadata.put("userName", obj.get("userName"));
                metadata.put("userId", obj.get("userId"));
                metadata.put("beginDate",obj.getDate("beginDate"));
                metadata.put("endDate",obj.getDate("endDate"));
                metadata.put("maxLimitTime",obj.getInt("maxLimitTime"));
                metadata.put("limitTime",obj.getInt("limitTime"));
                metadata.put("orgCode",obj.get("orgCode").toString());
                metadata.put("orgName",obj.get("orgName").toString());
                metadata.put("rootId",obj.get("rootId").toString());
                metadata.put("rootName",obj.get("rootName").toString());
                metadata.put("token",routeDefinition.getId());
                routeDefinition.setMetadata(metadata);
                log.info("==========服务映射开始==========");
                log.info("==========服务地址："+routeDefinition.getUri()+"==========");
                log.info("==========服务映射地址："+routeDefinition.getPathPatterns()+"==========");
                log.info("==========服务token："+routeDefinition.getId()+"==========");
                log.info("==========申请人账号："+routeDefinition.getMetadata().get("userId")+"==========");
                log.info("==========申请人姓名："+routeDefinition.getMetadata().get("userName")+"==========");
                dynamicRouteService.add(routeDefinition);
                log.info("==========服务映射结束==========");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.just("ok");
    }

    @GetMapping("/add")
    public Mono<String> add() {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(UUID.randomUUID().toString());
//        routeDefinition.setUri("http://127.0.0.1:8888/igs/rest/ogc/doc/410100DJQ/WMSServer");
        routeDefinition.setUri("http://172.16.103.200:8600/igs/rest/ogc/doc/410100DJQ/WMSServer");
        routeDefinition.setPathPatterns("/test");

        Map<String, String> rewritePath = new HashMap<>();
        rewritePath.put(routeDefinition.getPathPatterns(), URI.create(routeDefinition.getUri()).getPath());
        routeDefinition.setRewritePath(rewritePath);

        List<String> filters = new ArrayList<>();
        filters.add(OGCShareGatewayFilterFactory.getFilterName());
        routeDefinition.setFilters(filters);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userName", "张三");
//        metadata.put("type", "OGC");
        metadata.put("userId", IdUtil.randomUUID());
        metadata.put("token", "1234567");
        metadata.put("maxLimitTime", 8);
        metadata.put("orgCode", "1231231555");
        metadata.put("orgName", "单位机构");
        metadata.put("endDate", new Date());
        routeDefinition.setMetadata(metadata);
        dynamicRouteService.add(routeDefinition);
        return Mono.just("ok");
    }
}
