package com.point.clddock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.point.clddock.http.SprHttpClient;
import com.point.clddock.security.DockAES;
import com.point.clddock.security.DockBrige;
import com.point.clddock.security.DockRSA;
import com.point.clddock.util.CustomUtil;
import com.point.clddock.util.SprBeanUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static com.point.clddock.security.DockBrige.DOCK_PRIVATE_KEY;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ATest {
    @Resource
    private SprHttpClient sprHttpClient;

    /**
     * 测试Http
     */
    @Test
    public void testSprHttp() {
        String url = "http://zxcs.test.fxscm.net:8040/cldpoint/zyuDock/plat/getOutStoreData.do";
        Map<String, Object> bodyMap = CustomUtil.ofMap("name", "Big");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("uname", "sr");
        headerMap.put("passwd", "4862");
        sprHttpClient.syncPostJson4Cld(url, headerMap, bodyMap);
    }
    /**
     * 测试国际化
     */
    @Test
    public void testLocaleMessage() {
        String messageSource = SprBeanUtil.getMessageSource("result.success", null);
        System.out.println(messageSource);
    }
    /**
     * 测试对称加密
     */
    @Test
    public void testAES (){
        String uKey = DockAES.getUKey();
        String message = JSONObject.toJSONString(CustomUtil.ofMap("id", "007", "name", "zx"));
        String encryptMsg = DockAES.encryptMsg(message, uKey);
        String decrypt = DockAES.decrypt(encryptMsg, uKey);
        System.out.println(encryptMsg);
        System.out.println(decrypt);
    }
    /**
     * 测试Dock对接第三方接口
     */
    @Test
    public void testAuth(){

        String uKey = DockAES.getUKey();
        String return_publicKeyEncrypt = null;
        try {
            return_publicKeyEncrypt = DockRSA.publicKeyEncrypt(DockBrige.CLD_PUBLIC_KEY, uKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // AES加密传输数据
        Map<String, String> userMap = CustomUtil.ofMap("userId", "a007", "userName", "admin", "userPwd", "0000");
        String userInfo = JSON.toJSONString(userMap);
        String return_encryptMsg = DockAES.encryptMsg(userInfo, uKey);
//        String url = "http://zxcs.test.fxscm.net:8040/cldpoint/zyuDock/plat/getOutStoreData.do";
        String url = "http://zxcs.test.fxscm.net:8040/cldpoint/zyuDock/plat/setDate2Dock.do";
        Map<String, String> headerMap = CustomUtil.ofMap(DockBrige.U_KEY, return_publicKeyEncrypt);
        Map<String, Object> bodyMap = CustomUtil.ofMap(DockBrige.U_DATA, return_encryptMsg);
        sprHttpClient.syncPostJson4Cld(url, headerMap, bodyMap);
    }
}
