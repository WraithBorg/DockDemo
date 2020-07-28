package com.point.clddock.http;

import com.alibaba.fastjson.JSONObject;
import com.point.clddock.result.DockResult;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * OkHttp3
 */
@Component
public class SprHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(SprHttpClient.class);
    private static final String SPR_UTF8 = "application/json;charset=utf-8";
    @Resource
    private OkHttpClient okHttpClient;
    /**
     * 同步请求 POST JSON
     */
    public void syncPostJson4Cld(String url, Map<String, String> headerMap, Map<String,Object> bodyMap) {
        String jsonStr = JSONObject.toJSONString(bodyMap);
        DockResult syncPostJson = syncPostJson(url, headerMap, jsonStr);

        DockResult msgResult = JSONObject.parseObject(syncPostJson.getMessage(), DockResult.class);
        if (msgResult.ok()){
            logger.error("接受消息成功！");
        }else {
            logger.error("接受消息失败:" + msgResult.getMessage());
        }
        logger.error("消息传换:" + msgResult.toString());
    }
    /**
     * 同步请求 POST JSON
     */
    public DockResult syncPostJson(String url, Map<String, String> headerMap, String jsonStr) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(SPR_UTF8), jsonStr);
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
                String rtnJson = execute.body().string();
                logger.error("DOCK SUCCESS LOG -> url:{},json:{},return:{}", url, jsonStr, rtnJson);
                return DockResult.success(rtnJson);
            } else {
                logger.error("DOCK ERROR LOG START-> url:{},json:{},error:{}", url, jsonStr, execute.toString());
                logger.error("DOCK ERROR LOG END");
                return DockResult.fail(execute.toString());
            }
        } catch (IOException e) {
            logger.error("DOCK ERROR LOG START-> url:{},json:{}", url, jsonStr, e);
            e.printStackTrace();
            logger.error("DOCK ERROR LOG END");
            return DockResult.fail(e.getMessage());
        }
    }

    /**
     * 同步Get请求
     */
    public void syncGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                System.out.println(execute.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步GET请求
     */
    public void asyncGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                System.out.println(response.body().string());
            }
        });
    }

    /**
     * 同步POST请求
     */
    public void syncPost(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(new FormBody.Builder().build()).build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            if (execute.isSuccessful()) {
                System.out.println(execute.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步Post请求
     */
    public void asyncPost(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(new FormBody.Builder().build())
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

    /**
     * PUT请求
     */
    public void syncPut(String url) {
        Request put = new Request.Builder()
                .url(url)
                .put(new FormBody.Builder().build())
                .build();
    }

    /**
     * DELETE请求
     */
    public void syncDelete(String url) {
        Request delete = new Request.Builder()
                .url(url)
                .delete(new FormBody.Builder().build())
                .build();
    }

    /**
     * POST FORM
     */
    public void syncPostForm(String url, Map<String, String> formMap) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (formMap != null) for (String key : formMap.keySet()) {
            formBuilder.add(key, formMap.get(key));
        }
        FormBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            if (execute.isSuccessful()) {
                System.out.println(execute.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
