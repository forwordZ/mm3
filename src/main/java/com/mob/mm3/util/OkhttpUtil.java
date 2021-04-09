/**
 * Project Name:apptg-forapi-server
 * File Name:OkhttpUtil
 * Package Name: com.mob.awt.forapi.web.util
 * Date: 2020/2/27
 * Time: 10:27
 * User: zhangxq
 */

package com.mob.mm3.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:OkhttpUtil <br/>
 * ADD DESCRIPTION
 * <br/>
 * Date: 2020/2/27  <br/>
 * Time: 10:27  <br/>
 *
 * @author zhangxq
 */
public class OkhttpUtil {

    private static ConnectionPool cp = new ConnectionPool(100, 10, TimeUnit.MINUTES);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(cp)
            .build();

    public static JSONObject getJson(String url, JSONObject param) throws Throwable {
        return get(url, param, null);
    }

    public static String getStr(String url, JSONObject param, Map header) throws Throwable {
        Response response = getResponse(url,param,header);
        String resultStr = response.body().string();
        return resultStr;
    }

    public static JSONObject get(String url, JSONObject param, Map header) throws Throwable{
        JSONObject json = JSONObject.parseObject(getStr(url,param,header));
        return json;
    }

    public static Response getResponse(String url, JSONObject param, Map header) throws Throwable {
        StringJoiner stringJoiner = new StringJoiner("&", url + "?", "");
        if (param != null) {
            Set<String> keys = param.keySet();
            keys.forEach(li -> {
                Object value = param.get(li);
                String kv = li + "=" + value;
                stringJoiner.add(kv);
            });
            url = stringJoiner.toString();
        }
        if (header == null) {
            header = new HashMap();
        }
        Headers headers = Headers.of(header);
        Request request = new Request.Builder().url(url).headers(headers).get().build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
//            String resultStr = call.execute().body().string();
//            return JSONObject.parseObject(resultStr);
        return response;
    }

}
