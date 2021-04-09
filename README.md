# mm3
缓存更新通知框架，支持redis,zk

使用方式
1. 启动类增加 @EnableMM3RefreshCache(channel = ChannelEnum.ZK)，默认Redis
2. 实现 MM3RefreshCacheService接口
   重写init()初始化方法和refresh(Object)方法
   refresh方法会传入通知的数据
3. 在实现类上增加 @MM3RefreshCache 指定更新key，Redis的话需要指定间隔时间（单位：毫秒）
4. 更新方使用 MM3CacheUtil 修改值

