package com.point.clddock.controller;

import com.point.clddock.entity.JC_User;
import com.point.clddock.mapper.UserMapper;
import com.point.clddock.security.DockAES;
import com.point.clddock.security.DockBrige;
import com.point.clddock.security.DockRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private HttpServletRequest servletRequest;
    @Resource
    private UserMapper userMapper;

    @GetMapping("/")
    public Object index() {
        List<JC_User> jc_users = userMapper.selectList(null);
        jc_users.forEach(m-> System.out.println(m.toString()));
        return "123123";
    }

}
