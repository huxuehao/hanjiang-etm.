package com.hanjiang.etm.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * redis的配置类
 *
 * @author huxuehao
 **/
@Component
public class RedisConfig {
    @Bean
    @SuppressWarnings("all")
    /** 这就是一个自定义序列化模本,在开发中我们可以直接使用 */
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(factory);
        /* String 的序列化 */
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        /* key采用String的序列化方式 */
        template.setKeySerializer(stringRedisSerializer);
        /* hash的key也采用String的序列化方式 */
        template.setHashKeySerializer(stringRedisSerializer);
        /* value序列化方式采用jackson */
        template.setValueSerializer(stringRedisSerializer);
        // template.setValueSerializer(jackson2JsonRedisSerializer);
        /* hash的value序列化方式采用jackson */
        template.setHashValueSerializer(stringRedisSerializer);
        // template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
