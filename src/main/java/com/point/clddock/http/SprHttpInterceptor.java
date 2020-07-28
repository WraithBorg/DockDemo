package com.point.clddock.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * OkHttp3拦截器
 */
public class SprHttpInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SprHttpInterceptor.class);
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response =  chain.proceed(request);
        long endTime = System.currentTimeMillis();
        assert request.body() != null;
        logger.error(String.format("开始请求,请求url:%s,请求主体长度%s,请求头%s",request.url(),request.body().contentLength(),request.headers()));
        assert response.body() != null;
        logger.error(String.format("请求结束,相应时间：%s毫秒,响应数据长度：%s字节,响应头：%s",endTime-startTime,response.body().contentLength(),response.headers()));
        return response;
    }
}
