package com.point.clddock.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取容器内的Bean
 */
@Component
public class SprContext implements ApplicationContextAware {
    private static SprContext instance;
    private static ApplicationContext appContext;

    private static class SprContextHolder {
        private static SprContext sprContext = new SprContext();
    }

    public static SprContext getInstance() {
        return SprContextHolder.sprContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public List<Object> getBeansOfType(Class clazz) {
        List<Object> beans = new ArrayList<Object>();
        for (String name : appContext.getBeanNamesForType(clazz)) {
            beans.add(getBean(name));
        }
        return beans;
    }

    public Object getBean(String id) {
        return appContext.getBean(id);
    }

    public <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }
}
