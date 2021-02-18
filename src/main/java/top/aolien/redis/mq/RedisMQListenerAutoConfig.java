package top.aolien.redis.mq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aolien.redis.mq.core.RedisListenerAnnotationScanPostProcesser;
import top.aolien.redis.mq.core.RedisMessageQueueRegister;
import top.aolien.redis.mq.utils.RedisMQUtil;

@Configuration
@ConditionalOnProperty(prefix = "redis.queue.listener", name = "enable", havingValue = "true", matchIfMissing = true)
public class RedisMQListenerAutoConfig {

    @Bean
    public RedisListenerAnnotationScanPostProcesser redisListenerAnnotationScanPostProcesser(){
        return new RedisListenerAnnotationScanPostProcesser();
    }

    @Bean
    public RedisMessageQueueRegister redisMessageQueueRegister(){
        return new RedisMessageQueueRegister();
    }

    @Bean
    public RedisMQUtil redisMQUtil() {
        return new RedisMQUtil();
    }
}
