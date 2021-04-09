package com.mob.mm3.channel;


import com.mob.mm3.config.ZkConfig;
import com.mob.mm3.enums.ChannelEnum;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

public class ChannelFactory {

    private ApplicationContext applicationContext;
    private DefaultListableBeanFactory defaultListableBeanFactory;

    public ChannelFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

    public SyncDataChannel getChannel(ChannelEnum channelEnum) throws Exception {
        switch (channelEnum) {
            case REDIS: {
                createBean(RedisSyncChannel.class);
                return applicationContext.getBean(RedisSyncChannel.class);
            }
            case ZK: {
                createBean(ZkConfig.class);
                createBean(ZookeeperSyncChannel.class);
                return applicationContext.getBean(ZookeeperSyncChannel.class);
            }
        }
        throw new RuntimeException("channel type exception");
    }

    private void createBean(Class c) throws Exception{
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(defaultListableBeanFactory);
        annotatedBeanDefinitionReader.registerBean(c);
    }
}
