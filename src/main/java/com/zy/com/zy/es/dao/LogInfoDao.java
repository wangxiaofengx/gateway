package com.zy.com.zy.es.dao;


import com.zy.com.zy.es.entity.LogInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 
 * @author Administrator
 * @date 2021/12/24 14:03
 **/
 
public interface LogInfoDao extends ElasticsearchRepository<LogInfo, Long> {
    
}
