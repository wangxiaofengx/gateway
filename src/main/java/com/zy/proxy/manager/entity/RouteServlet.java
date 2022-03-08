package com.zy.proxy.manager.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteServlet implements Serializable {

    private String id;

    private String name;

    private String remark;

    private String sjlx;

    private String fwlx;

    private String url;

    private Integer sqsc;

    private Integer yqsc;

    private Integer maxTime;

    private Date gxrq;

    private Integer gxpl;

    private String pid;

    private String dtzbx;

    private String dtty;

    private String dtjd;

    private String fwdocName;

    private String fwfullPathName;

    private String fwgdbpUrl;

    private String fwid;

    private String fwlayer;

    private String fwlayerCode;

    private String fwlayerIndex;

    private String fwlegendUrl;

    private String fwloadAppUrl;

    private String fwloadType;

    private String fwpid;

    private String fwname;

    private Integer servletStatus;

    private Integer servletType;

    private List<?> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    public String getFwlx() {
        return fwlx;
    }

    public void setFwlx(String fwlx) {
        this.fwlx = fwlx;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSqsc() {
        return sqsc;
    }

    public void setSqsc(Integer sqsc) {
        this.sqsc = sqsc;
    }

    public Integer getYqsc() {
        return yqsc;
    }

    public void setYqsc(Integer yqsc) {
        this.yqsc = yqsc;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Date getGxrq() {
        return gxrq;
    }

    public void setGxrq(Date gxrq) {
        this.gxrq = gxrq;
    }

    public Integer getGxpl() {
        return gxpl;
    }

    public void setGxpl(Integer gxpl) {
        this.gxpl = gxpl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFwdocName() {
        return fwdocName;
    }

    public void setFwdocName(String fwdocName) {
        this.fwdocName = fwdocName;
    }

    public String getFwfullPathName() {
        return fwfullPathName;
    }

    public void setFwfullPathName(String fwfullPathName) {
        this.fwfullPathName = fwfullPathName;
    }

    public String getFwgdbpUrl() {
        return fwgdbpUrl;
    }

    public void setFwgdbpUrl(String fwgdbpUrl) {
        this.fwgdbpUrl = fwgdbpUrl;
    }

    public String getFwid() {
        return fwid;
    }

    public void setFwid(String fwid) {
        this.fwid = fwid;
    }

    public String getFwlayer() {
        return fwlayer;
    }

    public void setFwlayer(String fwlayer) {
        this.fwlayer = fwlayer;
    }

    public String getFwlayerCode() {
        return fwlayerCode;
    }

    public void setFwlayerCode(String fwlayerCode) {
        this.fwlayerCode = fwlayerCode;
    }

    public String getFwlayerIndex() {
        return fwlayerIndex;
    }

    public void setFwlayerIndex(String fwlayerIndex) {
        this.fwlayerIndex = fwlayerIndex;
    }

    public String getFwlegendUrl() {
        return fwlegendUrl;
    }

    public void setFwlegendUrl(String fwlegendUrl) {
        this.fwlegendUrl = fwlegendUrl;
    }

    public String getFwloadAppUrl() {
        return fwloadAppUrl;
    }

    public void setFwloadAppUrl(String fwloadAppUrl) {
        this.fwloadAppUrl = fwloadAppUrl;
    }

    public String getFwloadType() {
        return fwloadType;
    }

    public void setFwloadType(String fwloadType) {
        this.fwloadType = fwloadType;
    }

    public String getFwpid() {
        return fwpid;
    }

    public void setFwpid(String fwpid) {
        this.fwpid = fwpid;
    }

    public String getFwname() {
        return fwname;
    }

    public void setFwname(String fwname) {
        this.fwname = fwname;
    }

    public Integer getServletStatus() {
        return servletStatus;
    }

    public void setServletStatus(Integer servletStatus) {
        this.servletStatus = servletStatus;
    }

    public Integer getServletType() {
        return servletType;
    }

    public void setServletType(Integer servletType) {
        this.servletType = servletType;
    }

    public String getDtzbx() {
        return dtzbx;
    }

    public void setDtzbx(String dtzbx) {
        this.dtzbx = dtzbx;
    }

    public String getDtty() {
        return dtty;
    }

    public void setDtty(String dtty) {
        this.dtty = dtty;
    }

    public String getDtjd() {
        return dtjd;
    }

    public void setDtjd(String dtjd) {
        this.dtjd = dtjd;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }
}
