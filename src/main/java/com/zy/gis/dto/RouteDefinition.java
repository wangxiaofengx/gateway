package com.zy.gis.dto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 路由配置
 *
 * @Author wangxiaofeng
 * @Date 2020/09/30 11:25
 */
public class RouteDefinition {

    /**
     * 路由ID，由业务生成
     */
    private String id;

    /**
     * 映射路径
     */
    @NotNull
    private String pathPatterns;

    /**
     * 转发地址
     */
    @NotNull
    private String uri;

    /**
     * 重写路径：k为过滤内容,v为替换内容
     */
    private Map<String, String> rewritePath;

    /**
     * 动态添加请求参数
     */
    private Map<String, String> requestParameter;

    /**
     * 过滤器，class
     */
    private List<String> filters;

    private Map<String, Object> metadata;

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(String pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getRewritePath() {
        return rewritePath;
    }

    public void setRewritePath(Map<String, String> rewritePath) {
        this.rewritePath = rewritePath;
    }

    public Map<String, String> getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(Map<String, String> requestParameter) {
        this.requestParameter = requestParameter;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

}
