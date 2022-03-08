package com.zy.com.zy.es.service;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zy.com.zy.es.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @date 2021/12/28 10:52
 **/
@Service
@Slf4j
public class ServiceInfoService {

    Logger logger = LoggerFactory.getLogger(ServiceInfoService.class.getName());
    final ElasticsearchRestTemplate restTemplate;

    public ServiceInfoService(ElasticsearchRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 添加服务分类信息
     *
     * @param serviceType
     */
    public void saveServiceType(ServiceType serviceType) {
        testExistsIndex(ServiceType.class);
        long dataCount = getDataCount(serviceType.getRootId());
        if (dataCount > 0) {
            logger.info("分类已存在");
        } else {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(serviceType.getRootId())
                    .withObject(serviceType)
                    .build();
            IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceType.class);
            String documentId = restTemplate.index(indexQuery, indexCoordinates);
            log.info("test index add content : {}", documentId);

        }
    }

    public long getDataCount(String rootId) {
        Criteria criteria = new Criteria()
                .and("id").contains(rootId);
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceInfo.class);
        SearchHits<ServiceInfo> search = restTemplate.search(query, ServiceInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        return search.getTotalHits();
    }

    /**
     * 获取根节点数据
     * @return
     */
    public List<SearchHit<ServiceInfo>> getRootInfo() {
        Criteria criteria = new Criteria();
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceInfo.class);
        SearchHits<ServiceInfo> search = restTemplate.search(query, ServiceInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        List<SearchHit<ServiceInfo>> hitList = search.getSearchHits();
        return hitList;
    }


    public void addTestHourData() {
        HourReqInfo hourReqInfo = new HourReqInfo();
        Date nowDateTime = new Date();
        for (int i = 11; i < 12; i++) {
            DateTime dateTime = DateUtil.offsetHour(nowDateTime, i);
            //int hour = DateUtil.hour(dateTime, true);
            hourReqInfo.setReqDate(DateUtil.format(nowDateTime, "yyyy-MM-dd"));
            String hour = i + "";
            if (i < 10) {
                hour = "0".concat(hour);
            }
            hourReqInfo.setId(IdUtil.randomUUID());
            hourReqInfo.setReqDateHour(hour);
            saveHourReqInfo(hourReqInfo);
        }

    }

    public void addTestData() {
        //search5(ServiceReqInfo.class);
        JSONObject obj = getObj();

//        List<SearchHit<ServiceInfo>> rootInfo = getRootInfo();
//        rootInfo.forEach(s->{
//            String rootId = s.getContent().getRootId();
//            getReqDataCount(rootId);
//        });

        //资源ID
        String id = obj.getStr("ID");
        String name = obj.getStr("NAME");
        String rootId = obj.getStr("ROOT_ID");
        String rootName = obj.getStr("");

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId(IdUtil.randomUUID());
        serviceInfo.setServiceId(id);
        serviceInfo.setServiceName(name);
        serviceInfo.setRootId(rootId);
        saveServiceInfo(serviceInfo);

        //long data = getData(ServiceInfo.class);
        String dateStr = "2021-10-30";
        Date date = DateUtil.parse(dateStr);
        for (int i = 1; i < 30; i++) {
            ServiceReqInfo reqInfo = new ServiceReqInfo();
            reqInfo.setId(IdUtil.randomUUID());
            reqInfo.setServiceId(id);
            reqInfo.setServiceName(name);
            String nowDate = DateUtil.format(DateUtil.offsetDay(date, i), "yyyy-MM-dd");


            reqInfo.setReqDate(nowDate);
            reqInfo.setRootId(rootId);
            String orgId = "orgcode0" + i;
            reqInfo.setUserOrg(orgId);
            String orgName = "单位0" + i;
            reqInfo.setUserOrgName(orgName);
            reqInfo.setReqDateTime(DateUtil.formatDateTime(new Date()));
            saveServiceReqInfo(reqInfo);
            ServiceOrgReqInfo orgReqInfo = new ServiceOrgReqInfo();
            orgReqInfo.setUserOrg(orgId);
            orgReqInfo.setUserOrgName(orgName);
            saveServiceOrgReqInfo(orgReqInfo);

        }
        //long data1 = getData(ServiceReqInfo.class);
    }

    /**
     * 数据共享热度对比
     */
    public List<HashMap<String, Object>> getGxrddb() {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        List<SearchHit<ServiceInfo>> rootInfo = getRootInfo();
        rootInfo.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", s.getContent().getRootId());
            map.put("name", s.getContent().getServiceName());
            String rootId = s.getContent().getRootId();
            //String rootId = "ba17edcd-b4d4-40fa-ba97-fb6b0a95ea9b";
            List<SearchHit<ServiceReqInfo>> hitList = getReqServiceData(ReqData.builder()
                    .rootId(rootId)
                    .build()).getSearchHits();
            int sum = hitList.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("reqCount", sum);
            mapList.add(map);
        });
        return mapList;

    }


    /**
     * 获取共享数据详情
     */
    public List<HashMap<String, Object>> getGxsjxq() {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceInfo> serviceInfoData = (SearchHits<ServiceInfo>) getData(ServiceInfo.class);
        List<SearchHit<ServiceInfo>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String serviceId = s.getContent().getServiceId();
            map.put("serviceId", serviceId);
            map.put("serviceName", s.getContent().getServiceName());
            long dayReqCount = getReqServiceData(ReqData.builder()
                    .serviceId(serviceId)
                    .build()).getTotalHits();
            map.put("dayReqCount", dayReqCount);
            SearchHits<ServiceReqInfo> monthData = getMonthData(ReqData.builder()
                    .serviceId(serviceId)
                    .build());
            List<SearchHit<ServiceReqInfo>> hitList = monthData.getSearchHits();
            int sum = hitList.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("monthReqCount", sum);
            mapList.add(map);

        });
        return mapList;
    }

    /**
     * 数据共享热度明细分类
     */
    public List<HashMap<String, Object>> getSjgxrdmxfl(String startDate, String endDate) {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceType> serviceInfoData = (SearchHits<ServiceType>) getData(ServiceType.class);
        List<SearchHit<ServiceType>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String rootId = s.getContent().getRootId();
            map.put("id", rootId);
            map.put("name", s.getContent().getRootName());
            List<SearchHit<ServiceReqInfo>> hitList = getReqServiceData(ReqData.builder()
                    .rootId(rootId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()).getSearchHits();
            int sum = hitList.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("reqCount", sum);
            mapList.add(map);
        });
        return mapList;

    }

    /**
     * 数据共享热度明细
     */
    public List<HashMap<String, Object>> getSjgxrdmx(String startDate, String endDate, String rootId) {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceInfo> serviceInfoData = getServiceInfoData(rootId);

        List<SearchHit<ServiceInfo>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String serviceId = s.getContent().getServiceId();
            map.put("rootId", rootId);
            map.put("serviceId", s.getContent().getServiceId());
            map.put("serviceName", s.getContent().getServiceName());
            List<SearchHit<ServiceReqInfo>> hitList = getReqServiceData(ReqData.builder()
                    .rootId(rootId)
                    .serviceId(serviceId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()).getSearchHits();
            hitList.stream().forEach(ss -> {
                logger.info(JSONUtil.toJsonStr(ss));
            });
            int sum = hitList.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("reqCount", sum);
            mapList.add(map);
        });
        return mapList;

    }


    /**
     * 获取数据流向对比
     */
    public List<HashMap<String, Object>> getSjlxdb() {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceOrgReqInfo> serviceInfoData = (SearchHits<ServiceOrgReqInfo>) getData(ServiceOrgReqInfo.class);
        List<SearchHit<ServiceOrgReqInfo>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String userOrg = s.getContent().getUserOrg();
            map.put("userOrg", userOrg);
            map.put("userOrgName", s.getContent().getUserOrgName());

            SearchHits<ServiceReqInfo> searchHits = getReqServiceData(ReqData.builder()
                    .userOrg(userOrg)
                    .build());
            //当前天数据
            int dayReqCount = searchHits.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("dayReqCount", dayReqCount);

            SearchHits<ServiceReqInfo> monthData = getMonthData(ReqData.builder()
                    .userOrg(userOrg)
                    .build());
            List<SearchHit<ServiceReqInfo>> hitList = monthData.getSearchHits();
            int sum = hitList.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("monthReqCount", sum);
            mapList.add(map);

        });
        return mapList;
    }


    /**
     * 获取数据流向明细分类
     */
    public List<HashMap<String, Object>> getSjlxmxfl(String startDate, String endDate) {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceOrgReqInfo> serviceInfoData = (SearchHits<ServiceOrgReqInfo>) getData(ServiceOrgReqInfo.class);
        List<SearchHit<ServiceOrgReqInfo>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String userOrg = s.getContent().getUserOrg();
            map.put("userOrg", userOrg);
            map.put("userOrgName", s.getContent().getUserOrgName());
            long dayReqCount = getReqServiceData(ReqData.builder()
                    .userOrg(userOrg)
                    .startDate(startDate)
                    .startDate(endDate)
                    .build()).getTotalHits();
            map.put("dayReqCount", dayReqCount);
            mapList.add(map);

        });
        return mapList;
    }

    /**
     * 获取数据流向明细
     */
    public List<HashMap<String, Object>> getSjlxmx(String startDate, String endDate, String userOrg) {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        SearchHits<ServiceOrgReqInfo> serviceInfoData = (SearchHits<ServiceOrgReqInfo>) getData(ServiceOrgReqInfo.class);
        List<SearchHit<ServiceOrgReqInfo>> searchHitList = serviceInfoData.getSearchHits();
        searchHitList.forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            String userOrgId = s.getContent().getUserOrg();
            map.put("userOrg", userOrgId);
            map.put("userOrgName", s.getContent().getUserOrgName());
            List<SearchHit<ServiceReqInfo>> searchHits = getReqServiceData(ReqData.builder()
                    .userOrg(userOrg)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()).getSearchHits();
            int sum = searchHits.stream().mapToInt(ss -> ss.getContent().getReqCount()).sum();
            map.put("reqCount", sum);
            mapList.add(map);

        });
        return mapList;
    }

    /**
     * 获取各时段请求数据
     * @param dataStr
     * @return
     */
    public List<HashMap<String, Object>> getHourReqData(String dataStr) {
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(dataStr)) {
            boolQuery.must(QueryBuilders.matchQuery("reqDate", dataStr));
        } else {
            dataStr = DateUtil.format(new Date(), "yyyy-MM-dd");
            boolQuery.must(QueryBuilders.matchQuery("reqDate", dataStr));
        }
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(HourReqInfo.class);
        SearchHits<HourReqInfo> searchHits = restTemplate.search(query, HourReqInfo.class, indexCoordinates);
        List<SearchHit<HourReqInfo>> hitList = searchHits.getSearchHits();
        hitList.stream().forEach(s -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", s.getContent().getId());
            map.put("reqHour", s.getContent().getReqDateHour());
            map.put("reqDate", s.getContent().getReqDate());
            map.put("reqCount", s.getContent().getReqCount());
            mapList.add(map);
        });
        return mapList;
    }


    public SearchHits<ServiceReqInfo> getReqServiceData(ReqData reqData) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(reqData.getStartDate()) && StrUtil.isNotEmpty(reqData.getEndDate())) {
            RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("reqDate").from(reqData.getStartDate()).to(reqData.getEndDate()).includeUpper(true).includeLower(true);
            boolQuery.must(queryBuilder);
        } else if (StrUtil.isEmpty(reqData.getDateStr())) {
            reqData.setDateStr(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }

        if (StrUtil.isNotEmpty(reqData.getServiceId())) {
            boolQuery.must(QueryBuilders.matchQuery("serviceId", reqData.getServiceId()));
        }
        if (StrUtil.isNotEmpty(reqData.getRootId())) {
            boolQuery.must(QueryBuilders.matchQuery("rootId", reqData.getRootId()));
            log.info("rootId----{}", reqData.getRootId());
        }
        if (StrUtil.isNotEmpty(reqData.getUserOrg())) {
            boolQuery.must(QueryBuilders.matchQuery("userOrg", reqData.getUserOrg()));
        }
        if (StrUtil.isNotEmpty(reqData.getDateStr())) {
            boolQuery.must(QueryBuilders.matchQuery("reqDate", reqData.getDateStr()));
        }
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceReqInfo.class);
        SearchHits<ServiceReqInfo> search = restTemplate.search(query, ServiceReqInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        //return search.getTotalHits();
        return search;
    }

    public Object getData(Class<?> clazz) {
        Criteria criteria = new Criteria();
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(clazz);
        SearchHits<?> searchHits = restTemplate.search(query, clazz, indexCoordinates);
        log.info("search1----{}", searchHits.getTotalHits());
        //return searchHits.getTotalHits();
        return searchHits;
    }

    /**
     * 根据根节点查询服务信息
     *
     * @param rootId
     * @return
     */
    public SearchHits<ServiceInfo> getServiceInfoData(String rootId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(rootId)) {
            boolQuery.must(QueryBuilders.matchQuery("rootId", rootId));
        }
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceInfo.class);
        SearchHits<ServiceInfo> searchHits = restTemplate.search(query, ServiceInfo.class, indexCoordinates);
        log.info("search1----{}", searchHits.getTotalHits());
        //return searchHits.getTotalHits();
        return searchHits;
    }


    /**
     * 当月服务请求数据
     *
     * @param
     * @param reqData
     * @return
     */
    public SearchHits<ServiceReqInfo> getMonthData(ReqData reqData) {
        Calendar cal = Calendar.getInstance();
        if (StrUtil.isNotEmpty(reqData.getDateStr())) {
            cal.setTime(new Date());
        } else {
            cal.setTime(new Date());
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String monthFirst = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        String monthLast = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(reqData.getServiceId())) {
            boolQuery.must(QueryBuilders.matchQuery("serviceId", reqData.getServiceId()));
        }
        if (StrUtil.isNotEmpty(reqData.getRootId())) {
            boolQuery.must(QueryBuilders.matchQuery("rootId", reqData.getRootId()));
        }
        if (StrUtil.isNotEmpty(reqData.getUserOrg())) {
            boolQuery.must(QueryBuilders.matchQuery("userOrg", reqData.getUserOrg()));
        }
        if (StrUtil.isNotEmpty(monthFirst) && StrUtil.isNotEmpty(monthLast)) {
            RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("reqDate").from(monthFirst).to(monthLast).includeUpper(true).includeLower(true);

            boolQuery.must(queryBuilder);
        }
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceReqInfo.class);
        SearchHits<ServiceReqInfo> search = restTemplate.search(query, ServiceReqInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        return search;
    }

    /**
     * 添加服务信息
     *
     * @param serviceInfo
     */
    public void saveServiceInfo(ServiceInfo serviceInfo) {
        //判断索引是不否存在，不存在创建
        testExistsIndex(ServiceInfo.class);
        Criteria criteria = new Criteria()
                .and("serviceId").is(serviceInfo.getServiceId());
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceInfo.class);
        SearchHits<ServiceInfo> search = restTemplate.search(query, ServiceInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        if (search.getTotalHits() > 0) {
            log.info("资源----{}已存在", serviceInfo.getServiceName());
            Integer reqCount = search.getSearchHit(0).getContent().getReqCount();
            reqCount = reqCount + 1;
            updateReqCount(ServiceInfo.class, search.getSearchHit(0).getId(), reqCount);
            //更新总请求次数
        } else {
            serviceInfo.setReqCount(1);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(serviceInfo.getId())
                    .withObject(serviceInfo)
                    .build();
            IndexCoordinates indexServiceInfo = restTemplate.getIndexCoordinatesFor(ServiceInfo.class);
            String documentId = restTemplate.index(indexQuery, indexServiceInfo);
            log.info("index service_info content : {}添加成功", documentId);
        }

    }

    public void saveHourReqInfo(HourReqInfo hourReqInfo) {
        //判断索引是不否存在，不存在创建
        testExistsIndex(HourReqInfo.class);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(hourReqInfo.getReqDate())) {
            boolQuery.must(QueryBuilders.matchQuery("reqDate", hourReqInfo.getReqDate()));
        }
        if (StrUtil.isNotEmpty(hourReqInfo.getReqDateHour())) {
            boolQuery.must(QueryBuilders.matchQuery("reqDateHour", hourReqInfo.getReqDateHour()));
        }
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(HourReqInfo.class);
        SearchHits<HourReqInfo> search = restTemplate.search(query, HourReqInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        if (search.getTotalHits() > 0) {
            log.info("资源----{}已存在", hourReqInfo.getReqDateHour());
            Integer reqCount = search.getSearchHit(0).getContent().getReqCount();
            reqCount = reqCount + 1;
            updateReqCount(HourReqInfo.class, search.getSearchHit(0).getId(), reqCount);
            //更新总请求次数
        } else {
            hourReqInfo.setReqCount(1);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(hourReqInfo.getId())
                    .withObject(hourReqInfo)
                    .build();
            IndexCoordinates indexServiceInfo = restTemplate.getIndexCoordinatesFor(HourReqInfo.class);
            String documentId = restTemplate.index(indexQuery, indexServiceInfo);
            log.info("index hour_req_info content : {}添加成功", documentId);
        }

    }

    /**
     * 更新资源请求数据
     *
     * @param clazz
     * @param id
     * @param reqCount
     */
    public void updateReqCount(Class<?> clazz, String id, Integer reqCount) {
        Document document = Document.create().append("reqCount", reqCount);
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                //冲突重试
                .withRetryOnConflict(1)
                .build();
        //等同于 IndexCoordinates.of("book");笔者只是不想硬编码
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(clazz);
        UpdateResponse response = restTemplate.update(updateQuery, indexCoordinates);
        log.info("test index update content : {}", response.getResult());

    }

    /**
     * 添加更新服务请求信息
     *
     * @param reqInfo
     */
    public void saveServiceReqInfo(ServiceReqInfo reqInfo) {
        //判断索引是不否存在，不存在创建
        testExistsIndex(ServiceReqInfo.class);
        Criteria criteria = new Criteria()
                .and("reqDate").is(reqInfo.getReqDate())
                .and("serviceId").is(reqInfo.getServiceId());
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceReqInfo.class);
        SearchHits<ServiceReqInfo> search = restTemplate.search(query, ServiceReqInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        if (search.getTotalHits() > 0) {
            log.info("资源----{}已存在", reqInfo.getServiceName());
            //更新当天的请求数量
            Integer reqCount = search.getSearchHit(0).getContent().getReqCount();
            reqCount = reqCount + 1;
            updateReqCount(ServiceReqInfo.class, search.getSearchHit(0).getId(), reqCount);
            //更新总请求次数
        } else {
            reqInfo.setReqCount(1);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(reqInfo.getId())
                    .withObject(reqInfo)
                    .build();
            IndexCoordinates indexServiceReqInfo = restTemplate.getIndexCoordinatesFor(ServiceReqInfo.class);
            String documentId = restTemplate.index(indexQuery, indexServiceReqInfo);
            log.info("index service_req_info content : {}添加成功", documentId);
        }

    }

    /**
     * 添加更新单位服务请求统计信息
     *
     * @param orgReqInfo
     */
    public void saveServiceOrgReqInfo(ServiceOrgReqInfo orgReqInfo) {
        //判断索引是不否存在，不存在创建
        testExistsIndex(ServiceOrgReqInfo.class);
        Criteria criteria = new Criteria()
                .and("userOrg").is(orgReqInfo.getUserOrg());
        Query query = new CriteriaQuery(criteria);
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(ServiceOrgReqInfo.class);
        SearchHits<ServiceOrgReqInfo> search = restTemplate.search(query, ServiceOrgReqInfo.class, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        if (search.getTotalHits() > 0) {
            log.info("资源----{}已存在", orgReqInfo.getUserOrgName());
            //更新当天的请求数量
            Integer reqCount = search.getSearchHit(0).getContent().getReqCount();
            reqCount = reqCount + 1;
            updateReqCount(ServiceOrgReqInfo.class, search.getSearchHit(0).getId(), reqCount);
            //更新总请求次数
        } else {
            orgReqInfo.setReqCount(1);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(orgReqInfo.getUserOrg())
                    .withObject(orgReqInfo)
                    .build();
            IndexCoordinates indexServiceReqInfo = restTemplate.getIndexCoordinatesFor(ServiceOrgReqInfo.class);
            String documentId = restTemplate.index(indexQuery, indexServiceReqInfo);
            log.info("index service_req_info content : {}添加成功", documentId);
        }

    }


    public SearchHits<?> search5(Class<?> clazz) {
        //闭区间查询
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("reqDate").from("2021-10-01").to("2021-11-01");
        //开区间查询，默认是true，也就是包含
        QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2").includeUpper(false).includeLower(false);
        //大于
        QueryBuilders.rangeQuery("fieldName").gt("fieldValue");
        //大于等于
        QueryBuilders.rangeQuery("fieldName").gte("fieldValue");
        //小于
        QueryBuilders.rangeQuery("fieldName").lt("fieldValue");
        //小于等于
        QueryBuilders.rangeQuery("fieldName").lte("fieldValue");


        Query query = new NativeSearchQueryBuilder()
                //对该部分进行替换即可 (利用上方的QueryBuilders构建方式)
                //.withQuery(QueryBuilders.matchQuery("book_name", "程序员大黑鱼-1"))
                .withQuery(queryBuilder)
                .build();
        IndexCoordinates indexCoordinates = restTemplate.getIndexCoordinatesFor(clazz);
        SearchHits<?> search = restTemplate.search(query, clazz, indexCoordinates);
        log.info("search1----{}", search.getTotalHits());
        return search;
    }


    public void testCreateIndex(Class<?> clazz) {
        boolean isCreated = restTemplate.indexOps(ServiceInfo.class).create();
        log.info("test create index : {}", isCreated);
    }

    /**
     * 查询索引是否存在
     */

    public void testExistsIndex(Class<?> clazz) {
        boolean isExists = restTemplate.indexOps(clazz).exists();
        log.info("test exists index : {}", isExists);
        if (!isExists) {
            boolean isCreated = restTemplate.indexOps(clazz).create();
            log.info("test create index : {}", isCreated);
        }
    }


    /**
     * 获取测试数据
     *
     * @return
     */
    public JSONObject getObj() {
        String path = this.getClass().getClassLoader().getResource("data.json").getPath();
        File file = new File(path);
        String jsonFile = readJsonFile(path);
        JSONObject jsonObject = JSONUtil.parseObj(jsonFile);
        JSONArray jsonArray = JSONUtil.parseArray(jsonObject.getStr("RECORDS"));
        List<JSONObject> objectList = new ArrayList<>();
        jsonArray.forEach(s -> {
            JSONObject obj = JSONUtil.parseObj(s);
            if (obj.get("SERVLET_TYPE").equals("1")) {
                objectList.add(obj);
            }
        });
        Random random = new Random();
        int number = random.nextInt(objectList.size());
        JSONObject object = JSONUtil.parseObj(objectList.get(number));
        return object;
    }

    //读取json文件
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
