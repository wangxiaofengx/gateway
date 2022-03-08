package com.zy.com.zy.es.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 日志实体类
 * @author Administrator
 * @date 2021/12/24 11:48
 **/
@Data
@Document(indexName = "log_info")
public class LogInfo {
    @Id
    @Field(type = FieldType.Text)
    private String id;
    /**
     * 用户ID
     */
    @Field(type = FieldType.Text)
    private String userId;

    /**
     * 服务TOKEN
     */
    @Field(type = FieldType.Text)
    private String serviceToken;
    /**
     * 服务TOKEN
     */
    @Field(type = FieldType.Text)
    private String serviceId;
    /**
     * 服务TOKEN
     */
    @Field(type = FieldType.Text)
    private String serviceName;
    /**
     * 用户名称
     */
    @Field(type = FieldType.Keyword)
    private String userName;
    /**
     * 用户组织
     */
    @Field(type = FieldType.Keyword)
    private String userOrg;
    /**
     * 用户组织
     */
    @Field(type = FieldType.Keyword)
    private String userOrgName;
    /**
    /**
     * 资源名称
     */
    @Field(type = FieldType.Keyword)
    private String name;
    /**
     /**
     * 资源根目录名称
     */
    @Field(type = FieldType.Keyword)
    private String RootName;
    /**
     /**
     * 资源根目录ID
     */
    @Field(type = FieldType.Keyword)
    private String RootId;
    /**
     * 资源名称
     */
    @Field(type = FieldType.Text)
    private String reqIp ;

    /**
     * 请求日期
     */
    @Field(type = FieldType.Text)
    private String reqDate;


}

