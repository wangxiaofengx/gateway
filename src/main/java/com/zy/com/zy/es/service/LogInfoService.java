package com.zy.com.zy.es.service;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zy.com.zy.es.dao.LogInfoDao;
import com.zy.com.zy.es.entity.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @date 2021/12/24 14:09
 **/
@Service
@Slf4j
public class LogInfoService {
    Logger logger = LoggerFactory.getLogger(LogInfoService.class.getName());

    final ElasticsearchRestTemplate restTemplate;
    final LogInfoDao logInfoDao;
    final ServiceInfoService serviceInfoService;


    public LogInfoService(ElasticsearchRestTemplate restTemplate, LogInfoDao logInfoDao, ServiceInfoService serviceInfoService) {
        this.restTemplate = restTemplate;
        this.logInfoDao = logInfoDao;
        this.serviceInfoService = serviceInfoService;
    }

    /**
     * 根据serverToken获取数量
     *
     * @param serverToken
     * @return
     */
    public long getReqCount(String serverToken) {
        Criteria criteria = new Criteria()
                .and("serviceToken").contains(serverToken);
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(LogInfo.class);
        SearchHits<LogInfo> search = restTemplate.search(query, LogInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        return search.getTotalHits();
    }


    /**
     * 添加日志到ES
     *
     * @param logInfo
     */
    public void saveData(LogInfo logInfo) {

        Date nowDateTime = new Date();
        String nowDateStr = DateUtil.format(nowDateTime, "yyyy-MM-dd");


        ServiceType serviceType = new ServiceType();
        serviceType.setRootId(logInfo.getRootId());
        serviceType.setRootName(logInfo.getRootName());
        try {
            serviceInfoService.saveServiceType(serviceType);
        } catch (Exception ex) {
            log.error("根节点{}--{}", logInfo.getRootName(), new Date());
        }

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId(IdUtil.randomUUID());
        serviceInfo.setRootId(logInfo.getRootId());
        serviceInfo.setServiceId(logInfo.getServiceId());
        serviceInfo.setServiceName(logInfo.getServiceName());

        try {
            serviceInfoService.saveServiceInfo(serviceInfo);
        } catch (Exception ex) {
            log.error("根节点{}--{}--{}", logInfo.getServiceId(), logInfo.getServiceName(), new Date());
        }

        ServiceReqInfo reqInfo = new ServiceReqInfo();
        reqInfo.setId(IdUtil.randomUUID());
        reqInfo.setServiceId(logInfo.getServiceId());
        reqInfo.setServiceName(logInfo.getServiceName());
        reqInfo.setReqDate(nowDateStr);
        reqInfo.setRootId(logInfo.getRootId());
        reqInfo.setUserOrg(logInfo.getUserOrg());
        reqInfo.setUserOrgName(logInfo.getUserOrgName());
        reqInfo.setReqDateTime(DateUtil.formatDateTime(nowDateTime));
        reqInfo.setUserId(logInfo.getUserId());
        reqInfo.setReqIp(logInfo.getReqIp());

        try {
            serviceInfoService.saveServiceReqInfo(reqInfo);
        } catch (Exception ex) {
            log.error("根节点{}--{}--{}", logInfo.getServiceId(), logInfo.getServiceName(), new Date());
        }

        ServiceOrgReqInfo orgReqInfo = new ServiceOrgReqInfo();
        orgReqInfo.setId(IdUtil.randomUUID());
        orgReqInfo.setUserOrg(logInfo.getUserOrg());
        orgReqInfo.setUserOrgName(logInfo.getUserOrgName());

        try {
            serviceInfoService.saveServiceOrgReqInfo(orgReqInfo);
        } catch (Exception ex) {
            log.error("根节点{}--{}--{}", logInfo.getServiceId(), logInfo.getServiceName(), new Date());
        }

        HourReqInfo hourReqInfo = new HourReqInfo();
        hourReqInfo.setId(IdUtil.randomUUID());
        int hour = DateUtil.hour(new Date(), true);
        hourReqInfo.setReqDateHour(hour + "");
        hourReqInfo.setReqDate(nowDateStr);
        try {
            serviceInfoService.saveHourReqInfo(hourReqInfo);
        } catch (Exception ex) {
            log.error("根节点{}--{}--{}", logInfo.getServiceId(), logInfo.getServiceName(), new Date());
        }

    }
}
