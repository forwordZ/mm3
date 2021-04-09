package com.mob.mm3.channel;

import com.mob.mm3.notify.MM3RefreshCacheService;

public interface SyncDataChannel {

    void watch(MM3RefreshCacheService mm3RefreshCacheService) throws Exception;
}
