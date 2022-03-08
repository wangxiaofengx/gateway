package com.zy.proxy.manager.entity;

import java.util.Date;

/**
 * 和权限网关相互传值的实体类
 */
public class RouteCommServlet {

    /**
     *路由ID
     */
    private String token;

    /**
     *服务ID
     */
    private String servletId;

    /**
     * 服务名字
     */
    private String servletName;

    /**
     *服务地址
     */
    private String servletUrl;

    /**
     * 封装url
     */
    private String url;

    /**
     *使用者ID
     */
    private String userId;

    /**
     *使用者姓名
     */
    private String userName;

    /**
     * 申请单位代码
     */
    private String orgCode;

    /**
     * 申请单位名称
     */
    private String orgName;

    /**
     *限制单位
     */
    private String limitUnit;

    /**
     *
     限制次数
     */
    private Integer limitTime;

    /**
     *总限制次数
     */
    private Integer maxLimitTime;

    /**
     *限制开始时间
     */
    private Date beginDate;

    /**
     *限制结果时间
     */
    private Date endDate;

    private String rootId;

    private String rootName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServletId() {
        return servletId;
    }

    public void setServletId(String servletId) {
        this.servletId = servletId;
    }

    public String getServletUrl() {
        return servletUrl;
    }

    public void setServletUrl(String servletUrl) {
        this.servletUrl = servletUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLimitUnit() {
        return limitUnit;
    }

    public void setLimitUnit(String limitUnit) {
        this.limitUnit = limitUnit;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public Integer getMaxLimitTime() {
        return maxLimitTime;
    }

    public void setMaxLimitTime(Integer maxLimitTime) {
        this.maxLimitTime = maxLimitTime;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }
}
