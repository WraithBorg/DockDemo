package com.point.clddock.security;

import com.alibaba.fastjson.JSON;
import com.point.clddock.util.CustomUtil;

import java.util.Map;

/**
 * 第三方对接加密工具
 */
public class DockBrige {
    private static final String DOCK_PUBLIC_KEY = "Dock方公钥";
    public static final String DOCK_PRIVATE_KEY = "Dock方私钥";
    public static final String CLD_PUBLIC_KEY = "第三方公钥";
    public static final String U_KEY = "ukey";
    public static final String U_DATA = "data";

    public static void main(String[] args) {
        try {
            Map thirdPartyData = demo4ThirdParty();
            demo4Dock(thirdPartyData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方对接dock方，并往dock方传输数据
     */
    static Map demo4ThirdParty() throws Exception {
        // dock方提供公钥
//        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI0jK0tZPsVS8WF86ook2MC5j2Tf7ile7rcnigWZRzHzM1insKeonKgXYceXBgQsqBKv5vWywYcoOMY9b9rh8ocCAwEAAQ==";
        // 随机生成AES密钥并进行RSA加密，然后放到head中传送给dock方
        String uKey = DockAES.getUKey();//生成AES密钥
        String return_publicKeyEncrypt = DockRSA.publicKeyEncrypt(CLD_PUBLIC_KEY, uKey);// 利用公钥 对AES密钥 进行RSA加密
        // AES加密传输数据
        Map<String, String> userMap = CustomUtil.ofMap("userId", "a007", "userName", "admin", "userPwd", "0000");
        String userInfo = JSON.toJSONString(userMap);
        String return_encryptMsg = DockAES.encryptMsg(userInfo, uKey);
        // 返回数据
        return CustomUtil.ofMap("header_ukey", return_publicKeyEncrypt, "body_data", return_encryptMsg);
    }

    /**
     * dock方对接第三方，并接受第三方的传输数据
     */
    static void demo4Dock(Map map) throws Exception {
        // 获取数据
        String header_ukey = String.valueOf(map.get("header_ukey"));
        String body_data = String.valueOf(map.get("body_data"));
        // dock方自己的私钥
//        String privateKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAjSMrS1k+xVLxYXzqiiTYwLmPZN/uKV7utyeKBZlHMfMzWKewp6icqBdhx5cGBCyoEq/m9bLBhyg4xj1v2uHyhwIDAQABAkBgizQsYKn9lNUlGKvfP4E9pDEgkcV6a0Pj6ZoSZl07HtjyM9vBf/5PVD0dT4rzPoEoBC7UvUtyk783SfQikm+BAiEA2jgQI3p4fE2QKSLoMXYk8CioG+KEOYgvFs5GEblLCWECIQClkq6zsQhWQ6Yimn3g9BRHdfafbij9s2Tj6a72W5H85wIgInZkJhY7XPYq5DQ9E0KjRsJE+lJlq2hUd3eQHVB0B0ECIF1p73TkCY8Z5a6aOTDkpFhY/oaekTPptZ88pYDK0we7AiAPlt/styoG87KjAw9XKiprVKt3Ib1p7ltFJjByoUaBSA==";
        // 私钥解密 并获取AES密钥
        String uKey = DockRSA.privateKeyDecrypt(DOCK_PRIVATE_KEY, header_ukey);
        // 利用AES密钥解密数据
        String decrypt = DockAES.decrypt(body_data, uKey);
        System.out.println("私钥解密：" + decrypt);
    }
}
