package com.zy.com.zy.es.entity;


import lombok.Builder;
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
@Document(indexName = "service_info")
public class ServiceInfo {
    /**
     /**
     * 资源ID
     */
    @Field(type = FieldType.Text)
    private String id;
    /**
     /**
     * 资源ID
     */
    @Field(type = FieldType.Keyword)
    private String serviceId;
    /**
     /**
     * 资源根名称
     */
    @Field(type = FieldType.Keyword)
    private String serviceName;
    /**
     /**
     * 资源根目录ID
     */
    @Field(type = FieldType.Text)
    private String rootId;

    /**
     * 请求次数
     */
    @Field(type = FieldType.Integer)
    private Integer reqCount;


}
