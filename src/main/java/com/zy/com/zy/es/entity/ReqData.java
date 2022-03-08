package com.zy.com.zy.es.entity;


import lombok.Builder;
import lombok.Data;

/**
 * @author Administrator
 * @date 2021/12/29 09:46
 **/
@Data
@Builder
public class ReqData {
    private String serviceId;
    private String rootId;
    private String dateStr;
    private String userOrg;
    private String startDate;
    private String endDate;
}
