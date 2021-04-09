package com.mob.mm3.manager;

import com.mob.mm3.anno.EnableMM3RefreshCache;
import com.mob.mm3.anno.MM3RefreshCache;
import com.mob.mm3.channel.ChannelFactory;
import com.mob.mm3.channel.SyncDataChannel;
import com.mob.mm3.notify.MM3RefreshCacheService;
import com.mob.mm3.util.MainApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class CacheManagerConfig {

    private final Logger logger = LoggerFactory.getLogger(CacheManagerConfig.class);

    private List<MM3RefreshCacheService> mm3RefreshCacheServiceList = new ArrayList<>();

    private ChannelFactory channelFactory;
    private ApplicationContext applicationContext;

    @Resource
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        channelFactory = new ChannelFactory(applicationContext);
    }

    @Bean
    public void createManagerList() throws Exception{
        Class mainClass = MainApplication.deduceMainApplicationClass();
        EnableMM3RefreshCache enableMM3RefreshCache = (EnableMM3RefreshCache) mainClass.getDeclaredAnnotation(EnableMM3RefreshCache.class);
        if(enableMM3RefreshCache == null) return;

        SyncDataChannel syncDataChannel = channelFactory.getChannel(enableMM3RefreshCache.channel());

        Map<String,MM3RefreshCacheService> serviceMap =
                applicationContext.getBeansOfType(MM3RefreshCacheService.class);
        if(serviceMap == null || serviceMap.isEmpty()) {
            logger.info("no have mm3RefreshCacheService impl");
            return;
        }
        mm3RefreshCacheServiceList.addAll(serviceMap.values());

        mm3RefreshCacheServiceList.forEach(cs -> {
            try {
                syncDataChannel.watch(cs);
                logger.info("{} load finish", cs.getClass().getSimpleName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
