package com.point.clddock.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 对接第三方相关常量类
 */
@Component
@PropertySource("classpath:cldconfig.properties")
public class CldConfigConst {
    public static String WEB_HOST;
    public static String GET_OUT_STORE_DATA;

    @Value("${cld.web_host}")
    public void setWebHost(String web_host) {
        WEB_HOST = web_host;
    }
    @Value("${cld.getOutStoreData}")
    public void setGetOutStoreData(String getOutstoreData) {
        GET_OUT_STORE_DATA = getOutstoreData;
    }
}
