package com.mob.mm3.anno;

import com.mob.mm3.enums.ChannelEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableMM3RefreshCache {

    ChannelEnum channel() default ChannelEnum.REDIS;
}
