package com.zy.com.zy.es.entity;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 服务信息
 * @author Administrator
 * @date 2021/12/28 10:41
 **/
@Data
@Document(indexName = "service_type")
public class ServiceType {
    /**
     /**
     * 资源根目录名称
     */
    @Field(type = FieldType.Keyword)
    private String rootName;
    /**
     /**
     * 资源根目录ID
     */
    @Field(type = FieldType.Keyword)
    private String rootId;

    /**
     * 请求日期
     */
    @Field(type = FieldType.Text)
    private String reqCount;

}
