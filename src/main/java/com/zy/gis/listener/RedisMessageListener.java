package com.zy.gis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.Topic;

public interface RedisMessageListener<T> {

    Topic getTopic();

    default void onMessage(Message message, byte[] pattern) {
    }

    default void onMessage(T message, byte[] pattern) {
    }
}
