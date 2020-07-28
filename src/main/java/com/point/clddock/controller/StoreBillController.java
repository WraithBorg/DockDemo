package com.point.clddock.controller;

import com.point.clddock.result.DockResult;
import com.point.clddock.security.DockAES;
import com.point.clddock.security.DockBrige;
import com.point.clddock.security.DockRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class StoreBillController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private HttpServletRequest servletRequest;


    @PostMapping("/auth2")
    public DockResult getData4Cld(@RequestBody Map map) {
        String uKey = servletRequest.getHeader(DockBrige.U_KEY);
        String body_data = String.valueOf(map.get(DockBrige.U_DATA));
        String decrypt = null;
        try {
            uKey = DockRSA.privateKeyDecrypt(DockBrige.DOCK_PRIVATE_KEY, uKey);
            decrypt = DockAES.decrypt(body_data, uKey);
            logger.error("接受Dock数据；" +decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DockResult.success("接受数据成功"+decrypt);
    }
}
