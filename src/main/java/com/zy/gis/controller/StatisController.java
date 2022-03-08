package com.zy.gis.controller;


import com.zy.com.zy.es.entity.ServiceType;
import com.zy.com.zy.es.service.LogInfoService;
import com.zy.com.zy.es.service.ServiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计接口
 *
 * @author Administrator
 * @date 2021/12/28 10:25
 **/

@RestController
@RequestMapping("/statis")
@Slf4j
public class StatisController {

    final LogInfoService logInfoService;

    final ServiceInfoService serviceInfoService;

    public StatisController(LogInfoService logInfoService, ServiceInfoService serviceInfoService) {
        this.logInfoService = logInfoService;
        this.serviceInfoService = serviceInfoService;
    }


    /**
     * 数据共享热度
     */
    @GetMapping("/addHour")
    public void addHour() {
        serviceInfoService.addTestHourData();
    }


    /**
     * 数据共享热度
     */
    @GetMapping("/add")
    public void sjgxrd() {

        serviceInfoService.addTestData();
        ServiceType serviceInfo = new ServiceType();
        ServiceType serviceInfo1 = new ServiceType();
        ServiceType serviceInfo2 = new ServiceType();
        ServiceType serviceInfo3 = new ServiceType();
        serviceInfo.setRootId("b3d8177c-0fcb-48c9-b70a-046582c0e614");
        serviceInfo.setRootName("规划数据");
        serviceInfo1.setRootId("a4cbcf35-8a77-440e-90bd-dd70228058c2");
        serviceInfo1.setRootName("稳定耕地分布图");
        serviceInfo2.setRootId("ddf0d42c-9e3c-4a4c-8e61-7f1bb21678e9");
        serviceInfo2.setRootName("管理数据");
        serviceInfo3.setRootId("ba17edcd-b4d4-40fa-ba97-fb6b0a95ea9b");
        serviceInfo3.setRootName("现状数据");

        serviceInfoService.getRootInfo();
        serviceInfoService.saveServiceType(serviceInfo);
        serviceInfoService.saveServiceType(serviceInfo1);
        serviceInfoService.saveServiceType(serviceInfo2);
        serviceInfoService.saveServiceType(serviceInfo3);

    }

    /**
     * 数据共享热度
     */
    @GetMapping("/gxrddb")
    public Object getGxrddb() {
        List<HashMap<String, Object>> gxrddb = serviceInfoService.getGxrddb();
        return gxrddb;
    }


    /**
     * 共享数据详情
     *
     * @return
     */
    @GetMapping("/gxsjxq")
    public Object getGxsjxq() {
        List<HashMap<String, Object>> gxrddb = serviceInfoService.getGxsjxq();
        return gxrddb;
    }

    /**
     * 数据共享热度明细分类
     */
    @GetMapping("/sjgxrdmxfl")
    public Object getSjgxrdmxfl(String startDate, String endDate) {
        List<HashMap<String, Object>> gxrddb = serviceInfoService.getSjgxrdmxfl(startDate, endDate);
        return gxrddb;
    }

    /**
    public Object getSjgxrdmxfl(String startDate,String endDate) {
        Map<String,Object> result = new HashMap<>();
        List<HashMap<String, Object>> gxrddb = serviceInfoService.getSjgxrdmxfl(startDate,endDate);
        result.put("data",gxrddb);
        return result;
    }    /**
     * 数据共享热度明细
     */
    @GetMapping("/sjgxrdmx")
    public Map<String,Object> getSjgxrdmx(String startDate,String endDate,String rootId) {
        Map<String,Object> result = new HashMap<>();
        List<HashMap<String, Object>> gxrddb = serviceInfoService.getSjgxrdmx(startDate,endDate, rootId);
        result.put("success",new Boolean(true));
        result.put("results",gxrddb);
        return result;
    }


    /**
     * 获取数据流向对比
     *
     * @return
     */
    @GetMapping("/sjlxdb")
    public Object getSjlxdb() {
        List<HashMap<String, Object>> sjlxdb = serviceInfoService.getSjlxdb();
        return sjlxdb;
    }

    /**
     * 获取数据流向明细分类
     *
     * @return
     */
    @GetMapping("/sjlxmxfl")
    public Object getSjlxmxfl(String startDate, String endDate) {
        List<HashMap<String, Object>> sjlxdb = serviceInfoService.getSjlxmxfl(startDate, endDate);
        return sjlxdb;
    }

    /**
     * 获取数据共享流向明细分类
     *
     * @return
     */
    @GetMapping("/sjlxmx")
    public Object getSjlxmx(String startDate,String endDate,String userOrg) {
        Map<String,Object> result = new HashMap<>();
        List<HashMap<String, Object>> sjlxdb = serviceInfoService.getSjlxmx(startDate, endDate,userOrg);
        result.put("success",new Boolean(true));
        result.put("results",sjlxdb);
        return result;
    }

    /**
     * 获取各时段请求数据
     *
     * @param strDate
     * @return
     */
    @GetMapping("/gsdsj")
    public Object getGsdsj(String strDate) {
        List<HashMap<String, Object>> sjlxdb = serviceInfoService.getHourReqData(strDate);
        return sjlxdb;
    }


}
