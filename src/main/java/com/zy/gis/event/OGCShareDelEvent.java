package com.zy.gis.event;

import com.zy.gis.bo.OGCShare;
import com.zy.gis.listener.RedisMessageListener;
import com.zy.gis.service.DynamicRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

/**
 * OGC共享服务删除事件
 *
 * @author wangxiaofeng
 * @date 2021/12/16 10:55
 */
@Component
public class OGCShareDelEvent implements RedisMessageListener<OGCShare> {

    private static final Logger log = LoggerFactory.getLogger(OGCShareDelEvent.class);

    final DynamicRouteService dynamicRouteService;

    public OGCShareDelEvent(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic("OGC-SHARE-DEL");
    }

    @Override
    public void onMessage(OGCShare message, byte[] pattern) {
    }
}
