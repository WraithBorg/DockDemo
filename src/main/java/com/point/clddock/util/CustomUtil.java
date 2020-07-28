package com.point.clddock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 自定义工具类
 */
public class CustomUtil {
    private static final Logger logger = LoggerFactory.getLogger(CustomUtil.class);

    private CustomUtil() {

    }

    /**
     * 对象转为String
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 判断集合类是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合类是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否为非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数组是否为空
     */
    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * 判断数组是否为非空
     */
    public static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为非空
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断StringJoiner是否为空
     */
    public static boolean isBlank(StringJoiner stringJoiner) {
        return stringJoiner == null || stringJoiner.length() == 0;
    }

    /**
     * 判断StringJoiner是否为非空
     */
    public static boolean isNotBlank(StringJoiner stringJoiner) {
        return !isBlank(stringJoiner);
    }

    /**
     * 转为int
     */
    public static int toInt(Object obj) {
        return toInt(obj, -1);
    }

    public static int toInt(Object obj, int defaultValue) {
        String str;
        return isBlank(str = toString(obj)) ? defaultValue : Integer.parseInt(str);
    }

    /**
     * 转为BigDecimal
     */
    public static BigDecimal toBigDecimal(Object obj) {
        return toBigDecimal(obj, BigDecimal.ZERO);
    }

    public static BigDecimal toBigDecimal(Object obj, BigDecimal defaultValue) {
        String str;
        return isBlank(str = toString(obj)) ? defaultValue : new BigDecimal(str);
    }

    /**
     * 转为Boolean
     */
    public static boolean toBoolean(Object obj) {
        return Boolean.parseBoolean(toString(obj));
    }


    /**
     * 对数据进行分页
     *
     * @param data      数据源
     * @param startPage 页码
     * @param pageSize  每页数据量
     * @return 分页后的数据
     */
    public static <T> List<T> limit(Collection<T> data, int startPage, int pageSize) {
        if (isNotEmpty(data) && startPage > 0 && pageSize > 0) {
            return data.parallelStream()
                    .skip((startPage - 1) * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 生成List
     */
    @SafeVarargs
    public static <T> List<T> ofList(T... data) {
        if (isEmpty(data)) {
            return new ArrayList<>();
        }
        List<T> list;
        Collections.addAll(list = new ArrayList<>(data.length), data);
        return list;
    }

    /**
     * 生成Set
     */
    @SafeVarargs
    public static <T> Set<T> ofSet(T... data) {
        if (isEmpty(data)) {
            return new HashSet<>();
        }
        Set<T> set;
        Collections.addAll(set = new HashSet<>(data.length), data);
        return set;
    }

    /**
     * 生成map
     */
    public static <K, V> Map<K, V> ofMap(K k, V v) {
        return ofMapN(k, v);
    }

    public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2) {
        return ofMapN(k1, v1, k2, v2);
    }

    public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ofMapN(k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ofMapN(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ofMapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> ofMapN(Object... input) {
        //input的长度必须为2的整数倍
        if ((input.length & 1) != 0) {
            throw new IllegalArgumentException("长度必须为2的整数倍");
        }
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < input.length; i += 2) {
            K k = Objects.requireNonNull((K) input[i]);
            V v = (V) input[i + 1];
            map.put(k, v);
        }
        return map;
    }

    /**
     * 复制对象
     * 添加这个方法的原因是，apache中也有和Spring一样的方法BeanUtils.copyProperties，
     * 但是apache的有时候会有问题，通过CommonUtil中添加这个方法，统一为Spring的
     * 这方法不需要自己new一个target
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) throws Exception {
        Objects.requireNonNull(source);
        Objects.requireNonNull(clazz);
        Object target = clazz.newInstance();
        BeanUtils.copyProperties(source, target);
        return clazz.cast(target);
    }

    public static void requireNonNull(Object obj) {
        Objects.requireNonNull(obj);
        if (obj instanceof String) {
            if (isBlank((String) obj)) {
                throw new NullPointerException();
            }
        }
    }

    public static String formatDate(Date date, String pattern) {
        requireNonNull(date);
        requireNonNull(pattern);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(Object obj) throws ParseException {
        return parseDate(obj, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(Object obj, String pattern) throws ParseException {
        requireNonNull(obj);
        requireNonNull(pattern);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(toString(obj));
    }

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        return !isBlank(str) && str.matches("^([-+])?\\d+(\\.\\d+)?$");
    }

    public static boolean isNotNumeric(String str) {
        return !isNumeric(str);
    }

    /**
     * 过滤掉非法字符方法
     */
    public static String filterOffUtf8Mb4(String text) {
        try {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256;
                if ((b ^ 0xC0) >> 4 == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if ((b ^ 0xE0) >> 4 == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if ((b ^ 0xF0) >> 4 == 0) {
                    i += 4;
                }
            }
            buffer.flip();
            return new String(buffer.array(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("微信表情转码失败" + text, e);
            return "默认昵称";
        }
    }

    /**
     * 检验数量是否符合位数要求,不符合位数限制返回false
     */
    public static boolean checkAmountScale(String amount, int bscale, int escale) {
        String[] amounts = amount.split("\\.");
        if (amounts[0].length() > bscale) {
            return false;
        }
        if (amounts[1].length() > escale) {
            String test = amounts[1].substring(escale);
            BigDecimal decimal = new BigDecimal(test);
            return decimal.compareTo(BigDecimal.ZERO) <= 0;
        }
        return true;
    }

    public static String setAmountScale(String amount, int bscale) {
        String[] amounts = amount.split("\\.");
        String returnStr = "";
        String number = amounts[0];
        if (number.length() < bscale) {
            for (int i = number.length(); number.length() < bscale; i++) {
                number = "0" + number;
            }
            returnStr = returnStr + number;
        }
        if (amounts.length > 1) {
            returnStr = returnStr + amounts[1].substring(0, 4);
        } else {
            returnStr = returnStr + "0000";
        }
        return returnStr;
    }


    /**
     * 校验手机验证码
     */
    public static boolean confPhone(String phone){
        if (phone.length() != 11)
        {
            return false;
        }else{
            /**
             * 移动号段正则表达式
             */
            String pat1 = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$";
            /**
             * 联通号段正则表达式
             */
            String pat2  = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
            /**
             * 电信号段正则表达式
             */
            String pat3  = "^((133)|(153)|(177)|(18[0,1,9])|(149))\\d{8}$";
            /**
             * 虚拟运营商正则表达式
             */

            String pat4 = "^((170))\\d{8}|(1718)|(1719)\\d{7}$";

            Pattern pattern1 = Pattern.compile(pat1);
            Matcher match1 = pattern1.matcher(phone);
            boolean isMatch1 = match1.matches();
            if(isMatch1){
                return true;
            }
            Pattern pattern2 = Pattern.compile(pat2);
            Matcher match2 = pattern2.matcher(phone);
            boolean isMatch2 = match2.matches();
            if(isMatch2){
                return true;
            }
            Pattern pattern3 = Pattern.compile(pat3);
            Matcher match3 = pattern3.matcher(phone);
            boolean isMatch3 = match3.matches();
            if(isMatch3){
                return true;
            }
            Pattern pattern4 = Pattern.compile(pat4);
            Matcher match4 = pattern4.matcher(phone);
            boolean isMatch4 = match4.matches();
            if(isMatch4){
                return true;
            }
            return false;
        }
    }
}
