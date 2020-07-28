package com.point.clddock.security;

import org.springframework.util.StringUtils;

/**
 * 进制转换工具类
 */
public class DockHex {

    /**
     * 二进制转十六进制
     */
    public static String _2_16(byte[] bytes) {

        if (bytes == null || bytes.length == 0)
            return null;

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 16进制转化为 2进制
     */
    public static byte[] _16_2(String hexStr) {
        if (StringUtils.isEmpty(hexStr))
            return null;

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
