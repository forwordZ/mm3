package com.mob.mm3.channel;

import com.mob.mm3.anno.MM3RefreshCache;
import com.mob.mm3.notify.MM3RefreshCacheService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;

@Configuration
public class ZookeeperSyncChannel implements SyncDataChannel {

    @Resource
    private CuratorFramework curatorFramework;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void watch(MM3RefreshCacheService mm3RefreshCacheService) throws Exception {
        MM3RefreshCache mm3RefreshCache = mm3RefreshCacheService.getClass().getAnnotation(MM3RefreshCache.class);

        String key = applicationContext.getEnvironment().resolvePlaceholders(mm3RefreshCache.key());
        if (!key.startsWith("/")) {
            key = "/" + key;
        }

        PathChildrenCache pcc = new PathChildrenCache(curatorFramework, key, true);
        pcc.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        PathChildrenCacheListener pccl = (client, event) -> {
            if (event.getType() != CHILD_ADDED) return;
            if (event.getData() != null) {
                mm3RefreshCacheService.refresh(new String(event.getData().getData()));
            }
        };
        pcc.getListenable().addListener(pccl);
    }
}
