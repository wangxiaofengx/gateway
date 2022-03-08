package com.zy.gis.config;

import com.zy.gis.listener.RedisMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 消息队列  订阅者  redis配置
 *
 * @Author wangxiaofeng
 * @Date 2019/8/7 19:54
 */
@Configuration
public class RedisSubConfig {

    @Autowired(required = false)
    List<RedisMessageListener> messageListenerList;

    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(RedisConnectionFactory connectionFactory, RedisTemplate redisTemplate) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        GenericJackson2JsonRedisSerializer redisSerializer = (GenericJackson2JsonRedisSerializer) redisTemplate.getValueSerializer();

        messageListenerList.forEach(messageListener -> {
            container.addMessageListener((message, pattern) -> {
                Object obj;
                Type[] interfaces = messageListener.getClass().getGenericInterfaces();
                ParameterizedType pt = null;
                for (Type anInterface : interfaces) {
                    if (anInterface instanceof ParameterizedType && ((ParameterizedType) anInterface).getRawType() == RedisMessageListener.class) {
                        pt = (ParameterizedType) anInterface;
                        break;
                    }
                }
                if (pt != null && pt.getActualTypeArguments().length > 0) {
                    Class clazz = (Class) pt.getActualTypeArguments()[0];
                    if (clazz == String.class) {
                        obj = new String(message.getBody());
                    } else if (clazz == Byte.class) {
                        obj = Byte.valueOf(new String(message.getBody()));
                    } else if (clazz == Short.class) {
                        obj = Short.valueOf(new String(message.getBody()));
                    } else if (clazz == Integer.class) {
                        obj = Integer.valueOf(new String(message.getBody()));
                    } else if (clazz == Float.class) {
                        obj = Float.valueOf(new String(message.getBody()));
                    } else if (clazz == Double.class) {
                        obj = Double.valueOf(new String(message.getBody()));
                    } else if (clazz == Long.class) {
                        obj = Long.valueOf(new String(message.getBody()));
                    } else {
                        obj = redisSerializer.deserialize( message.getBody(), (Class) pt.getActualTypeArguments()[0]);
                    }
                } else {
                    obj = message.getBody();
                }

                messageListener.onMessage(obj, pattern);
                messageListener.onMessage(message, pattern);
            }, messageListener.getTopic());
        });
        return container;
    }
}
