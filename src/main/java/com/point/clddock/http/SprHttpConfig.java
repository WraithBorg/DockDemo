package com.point.clddock.http;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp3配置
 */
@Configuration
@PropertySource("classpath:okhttpconfig.properties")
public class SprHttpConfig {
    @Value("${okhttp.timeout:7000}")
    private Long timeout;
    @Value("${okhttp.maxConnection:200}")
    private Integer maxConnection;
    @Value("${okhttp.coreConnection:10}")
    private Integer coreConnection;
    @Value("${okhttp.resetConnection:false}")
    private boolean resetConnection;
    @Value("${okhttp.maxHostConnection:30}")
    private Integer maxHostConnection;


    @Bean
    public OkHttpClient okHttpClient(){
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(maxHostConnection);
        dispatcher.setMaxRequests(maxConnection);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .dispatcher(dispatcher)
                .retryOnConnectionFailure(resetConnection)
                .addInterceptor(new SprHttpInterceptor())
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(maxConnection,coreConnection,TimeUnit.MILLISECONDS))
                .build();
        return okHttpClient;
    }
}
