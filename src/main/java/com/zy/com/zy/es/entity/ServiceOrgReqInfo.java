package com.zy.com.zy.es.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 服务信息
 *
 * @author Administrator
 * @date 2021/12/28 10:41
 **/
@Data
@Document(indexName = "service_org_req_info")
public class ServiceOrgReqInfo {
    /**
     * /**
     * 资源ID
     */
    @Field(type = FieldType.Text)
    @Id
    private String id;
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


}
