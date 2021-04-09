package com.mob.mm3.channel;

import com.mob.mm3.anno.MM3RefreshCache;
import com.mob.mm3.notify.MM3RefreshCacheService;
import com.mob.mm3.util.TimeStampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
public class RedisSyncChannel implements SyncDataChannel {

    private final Logger logger = LoggerFactory.getLogger(RedisSyncChannel.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void watch(MM3RefreshCacheService mm3RefreshCacheService) throws Exception {
        MM3RefreshCache mm3RefreshCache = mm3RefreshCacheService.getClass().getAnnotation(MM3RefreshCache.class);

        String key = mm3RefreshCache.key();
        long waitTime = mm3RefreshCache.refreshTime();

        new Thread(() -> {
            long startTime = TimeStampUtil.get();
            while (true) {
                try {
                    Thread.sleep(waitTime);

                    long endTime = TimeStampUtil.get();

                    Set set = stringRedisTemplate.opsForZSet().rangeByScore(key, startTime, endTime);

                    if (CollectionUtils.isEmpty(set)) {
                        continue;
                    }

                    logger.info("sync key:{},value:{}", key, set);

                    set.forEach(li -> {
                        mm3RefreshCacheService.refresh(li);
                    });
                    startTime = endTime;
                } catch (Exception e) {
                    logger.error("sync data error-> key:{}", key, e);
                }
            }
        }).start();
    }
}
