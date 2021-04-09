package com.mob.mm3.util;

import com.alibaba.fastjson.JSONObject;

public class TimeStampUtil {

    private static final String URL = "http://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp";
    public static long get() {
        try {
            JSONObject resultJson = OkhttpUtil.getJson(URL,null);
            return resultJson.getJSONObject("data").getLong("t");
        } catch (Throwable t) {
        }

        return System.currentTimeMillis();
    }
}
