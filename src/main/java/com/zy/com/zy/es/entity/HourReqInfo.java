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
@Document(indexName = "hour_req_info")
public class HourReqInfo {
    /**
     * /**
     * 资源ID
     */
    @Field(type = FieldType.Text)
    @Id
    private String id;

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
     * 请求时段
     */
    @Field(type = FieldType.Text)
    private String reqDateHour;

}
