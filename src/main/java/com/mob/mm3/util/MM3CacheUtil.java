package com.mob.mm3.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MM3CacheUtil {

    @Resource
    public void setApplicationContext(ApplicationContext applicationContext) {
        MM3CacheUtil.applicationContext = applicationContext;
    }

    private static ApplicationContext applicationContext;

    public PutCache redis() {
        return Redis.redis;
    }

    public PutCache zookeeper() {
        return Zookeeper.zookeeper;
    }


    static class Redis implements PutCache {
        private static Redis redis = new Redis();

        public void addRefresh(String key, String value) {
            applicationContext.getBean(StringRedisTemplate.class).opsForZSet().add(key, value, TimeStampUtil.get());
        }
    }

    static class Zookeeper implements PutCache {
        private static Zookeeper zookeeper = new Zookeeper();

        public synchronized void addRefresh(String key, String value) {
            try {
                if(!key.startsWith("/")) {
                    key = "/" + key;
                }
                applicationContext.getBean(CuratorFramework.class)
                        .create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .forPath(key, value.getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
