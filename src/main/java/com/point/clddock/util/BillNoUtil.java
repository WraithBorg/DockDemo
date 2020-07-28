package com.point.clddock.util;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成单号工具类
 */
public class BillNoUtil {

    private static AtomicInteger orderNum = new AtomicInteger(1000);
    private static ConcurrentHashMap<String, String> conMap = new ConcurrentHashMap<>();//保存每天日期

	public static String createNo(){
		return createNo("DD");
	}
    public static String createNo(String code) {
        // 判断是否重置序号
        String ymDay = DDateUtil.YDAY_FORMATT.get().format(new Date());//年月日
        if (BillNoUtil.conMap.putIfAbsent(ymDay, "") == null) {//日期改变 序号重置
            BillNoUtil.orderNum = new AtomicInteger(1000);
        }
        // 生成单号
        String increment = String.valueOf(orderNum.getAndIncrement());
        return code + DDateUtil.DAY_FORMATTER.get().format(new Date()) + increment;
    }
}
