package com.mob.mm3.notify;

public interface MM3RefreshCacheService<T> {

    void refresh(T t);

    void init();
}
