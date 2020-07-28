package com.point.clddock.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.UUID;

/**
 * AES对称加密
 */
public class DockAES {
    private static int secretDigit = 128;
    private static int keyLength = secretDigit / 8;



    /**
     * 解密报文
     */
   public static String decrypt(String msg,  String ukey) {
        byte[] key = generatorAESKey(ukey);
        byte[] encrypted = Base64.getDecoder().decode(msg);
        byte[] decrypted;
        try {
            decrypted = decrypt(key, encrypted);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("解密报文失败", e);
        }
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // 加密:
    private static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }

    // 解密:
    private static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }

    /**
     * 生成随机密钥
     */
    public static String getUKey() {
        String striping = "-";
        String emptyStr = "";
        return UUID.randomUUID().toString().replace(striping, emptyStr).substring(0, keyLength);
    }

    /**
     * 创建AES密钥
     */
    private static byte[] generatorAESKey(String ukey) {
        return ukey.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 加密报文
     */
    public static String encryptMsg(String message, String ukey) {
        byte[] key = generatorAESKey(ukey);
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted;
        try {
            encrypted = encrypt(key, data);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("加密报文失败",e);
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
