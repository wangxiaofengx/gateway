package com.zy.gis.event;

import com.zy.gis.filter.PermissFilter;
import com.zy.gis.listener.RedisMessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;

//@Component
public class LayerRoleEvent implements RedisMessageListener<String> {
    @Override
    public Topic getTopic() {
        return new ChannelTopic("layer_role");
    }

    @Override
    public void onMessage(String message, byte[] pattern) {
        PermissFilter.permissMap.clear();
    }
}
