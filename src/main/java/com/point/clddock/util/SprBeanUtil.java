package com.point.clddock.util;

import com.point.clddock.component.SprContext;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class SprBeanUtil {
    public static String getMessageSource(String key, String[] arrStr) {
        MessageSource messageSource = SprContext.getInstance().getBean(MessageSource.class);
        String message = messageSource.getMessage(key, arrStr, Locale.SIMPLIFIED_CHINESE);
        return message;
    }
}
