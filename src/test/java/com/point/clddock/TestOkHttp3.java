package com.point.clddock;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Http工具类
 */
public class TestOkHttp3 {
    private static final Logger logger = LoggerFactory.getLogger(TestOkHttp3.class);

    public static void main(String[] args) {
        String url = "http://zxcs.test.fxscm.net:8040/cldpoint/zyuDock/plat/getOutStoreData.do";
        String jsonStr = "{\"name\":\"BigKang\"}";
        Map<String, String> headMap = new HashMap<>();
        headMap.put("uname", "sr");
        headMap.put("passwd", "4862");
        syncRequestJson(url, headMap, jsonStr);
    }

    /**
     * 同步请求 POST JSON
     */
    static void syncRequestJson(String url, Map<String, String> headerMap, String jsonStr) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request.Builder post = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (headerMap != null) for (String key : headerMap.keySet()) {
            post.addHeader(key, headerMap.get(key));
        }
        Request request = post.build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                logger.error("DOCK SUCCESS LOG -> url:{},json:{},return:{}", url, jsonStr, execute.body().string());
            } else {
                logger.error("DOCK ERROR LOG START-> url:{},json:{},error:{}", url, jsonStr, execute.toString());
                logger.error("DOCK ERROR LOG END");
            }
        } catch (IOException e) {
            logger.error("DOCK ERROR LOG START-> url:{},json:{}", url, jsonStr, e);
            e.printStackTrace();
            logger.error("DOCK ERROR LOG END");
        }
    }

    static void asynRequest() {
        String url = "http://zxcs.test.fxscm.net:8040/cldpoint/zyuDock/plat/get.do";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
