package top.aolien.redis.mq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册redis消息队列处理方法，@RedisListener注解扫描器
 */
public class RedisListenerAnnotationScanPostProcesser implements BeanPostProcessor {

    private static final List<RedisListenerMethod> candidates = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            AnnotationAttributes annotationAttributes = AnnotatedElementUtils
                    .findMergedAnnotationAttributes(method, RedisListener.class, false, false);
            if (null != annotationAttributes) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && RedisMessage.class.isAssignableFrom(parameterTypes[0])) {
                    String queueName = (String) annotationAttributes.get("queueName");
                    if (StringUtils.isEmpty(queueName)) {
                        throw new RuntimeException("在" + method + "方法上的注解@RedisListener没有设置参数queueName的值");
                    }
                    RedisListenerMethod rlm = new RedisListenerMethod();
                    rlm.setBeanName(beanName);
                    rlm.setQueueName(queueName);
                    rlm.setTargetMethod(method);
                    candidates.add(rlm);
                } else {
                    throw new RuntimeException("有@RedisListener注解的方法有且仅能包含一个RedisMessage类型的参数");
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public static List<RedisListenerMethod> getCandidates() {
        return candidates;
    }
}
