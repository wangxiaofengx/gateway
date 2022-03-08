package com.zy.com.zy.es.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 服务信息
 *
 * @author Administrator
 * @date 2021/12/28 10:41
 **/
@Data
@Document(indexName = "service_req_info")
public class ServiceReqInfo {
    /**
     * /**
     * 资源ID
     */
    @Field(type = FieldType.Text)
    @Id
    private String id;

    /**
     * 用户ID
     */
    @Field(type = FieldType.Text)
    private String userId;

    /**
     * /**
     * 资源ID
     */
    @Field(type = FieldType.Keyword)
    private String serviceId;
    /**
     * /**
     * 资源名称
     */
    @Field(type = FieldType.Keyword)
    private String serviceName;
    /**
     * /**
     * 资源根目录ID
     */
    @Field(type = FieldType.Keyword)
    private String rootId;
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
     * 请求次数
     */
    @Field(type = FieldType.Integer)
    private Integer reqCount;

    /**
     * 请求日期
     */
    @Field(type = FieldType.Text)
    private String reqDate;

    /**
     * 请求日期
     */
    @Field(type = FieldType.Text)
    private String reqDateTime;

    /**
     * 请求IP
     */
    @Field(type = FieldType.Text)
    private String reqIp;


}
