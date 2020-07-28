package com.point.clddock.security;


import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *  RSA非对称加密
 */
public class DockRSA {

    /*
    NoSuchAlgorithmException：无此加密算法
    InvalidKeyException：非法密钥
    NoSuchPaddingException：无明文数据
    BadPaddingException：明文数据损坏
    UnsupportedEncodingException：不支持的编码
    InvalidKeySpecException：密文格式不正确
    IllegalBlockSizeException：密文长度非法
    */

    private static final String RSA = "RSA";
    private static int LENGTH = 512;

    /**
     * 获取密钥
     *
     * @param keySize 64的整数倍
     * @return 默认先私钥后公钥
     */
    public static String[] getKey(int keySize) throws NoSuchAlgorithmException {
        LENGTH = keySize;
        return commonKey(LENGTH);
    }

    /**
     * 获取密钥
     *
     * @return 默认先私钥后公钥
     * @throws NoSuchAlgorithmException [ellipsis]
     */
    public static String[] getKey() throws NoSuchAlgorithmException {
        return commonKey(LENGTH);
    }

    /**
     * 私钥加密
     *
     * @param key       [ellipsis]
     * @param plainText [ellipsis]
     * @return [ellipsis]
     * @throws NoSuchAlgorithmException     [ellipsis]
     * @throws InvalidKeySpecException      [ellipsis]
     * @throws NoSuchPaddingException       [ellipsis]
     * @throws UnsupportedEncodingException [ellipsis]
     * @throws BadPaddingException          [ellipsis]
     * @throws IllegalBlockSizeException    [ellipsis]
     * @throws InvalidKeyException          [ellipsis]
     */
    public static String privateKeyEncrypt(String key, String plainText) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {
        PrivateKey privateKey = commonGetPrivatekeyByText(key);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return DockBase64.byteArrayToBase64(result);
    }

    /**
     * 公钥解密
     *
     * @param key        [ellipsis]
     * @param cipherText [ellipsis]
     * @return [ellipsis]
     * @throws NoSuchAlgorithmException  [ellipsis]
     * @throws InvalidKeySpecException   [ellipsis]
     * @throws NoSuchPaddingException    [ellipsis]
     * @throws InvalidKeyException       [ellipsis]
     * @throws BadPaddingException       [ellipsis]
     * @throws IllegalBlockSizeException [ellipsis]
     */
    public static String publicKeyDecrypt(String key, String cipherText) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        PublicKey publicKey = commonGetPublickeyByText(key);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(DockBase64.base64ToByteArray(cipherText));

        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param key       [ellipsis]
     * @param plainText [ellipsis]
     * @return [ellipsis]
     * @throws NoSuchAlgorithmException     [ellipsis]
     * @throws InvalidKeySpecException      [ellipsis]
     * @throws NoSuchPaddingException       [ellipsis]
     * @throws InvalidKeyException          [ellipsis]
     * @throws BadPaddingException          [ellipsis]
     * @throws IllegalBlockSizeException    [ellipsis]
     * @throws UnsupportedEncodingException [ellipsis]
     */
    public static String publicKeyEncrypt(String key, String plainText) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        PublicKey publicKey = commonGetPublickeyByText(key);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] result = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return DockBase64.byteArrayToBase64(result);
    }

    /**
     * 私钥解密
     *
     * @param key        [ellipsis]
     * @param cipherText [ellipsis]
     * @return [ellipsis]
     * @throws NoSuchAlgorithmException  [ellipsis]
     * @throws InvalidKeySpecException   [ellipsis]
     * @throws NoSuchPaddingException    [ellipsis]
     * @throws InvalidKeyException       [ellipsis]
     * @throws BadPaddingException       [ellipsis]
     * @throws IllegalBlockSizeException [ellipsis]
     */
    public static String privateKeyDecrypt(String key, String cipherText) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = commonGetPrivatekeyByText(key);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(DockBase64.base64ToByteArray(cipherText));

        return new String(result);
    }

    // (common)获取密钥
    private static String[] commonKey(int size) throws NoSuchAlgorithmException {
        String[] keys = new String[2];

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(size);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        keys[0] = DockBase64.byteArrayToBase64(rsaPrivateKey.getEncoded());
        // 公钥
        keys[1] = DockBase64.byteArrayToBase64(rsaPublicKey.getEncoded());

        return keys;
    }

    // (common)通过公钥文本获取公钥
    private static PublicKey commonGetPublickeyByText(String keyText)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = DockBase64.base64ToByteArray(keyText);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(x509KeySpec);
    }

    // (common)通过私钥文本获取私钥
    private static PrivateKey commonGetPrivatekeyByText(String keyText)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = DockBase64.base64ToByteArray(keyText);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 签名
     *
     * @param content    待签名内容
     * @param algorithm  签名算法
     * @param privateKey 私钥
     * @param charset    签名内容为中文时，你可能需要指定编码
     * @return 签名(String)
     * @throws NoSuchAlgorithmException     签名算法异常
     * @throws InvalidKeySpecException      密钥格式不正确
     * @throws InvalidKeyException          密钥异常
     * @throws SignatureException           签名异常
     * @throws UnsupportedEncodingException 可能你指定的编码不能正确读取你要签名的内容
     */
    public static String sign(String content, String algorithm, String privateKey, String charset)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        Signature signTool = Signature.getInstance(algorithm);
        PrivateKey key = commonGetPrivatekeyByText(privateKey);
        signTool.initSign(key);
        if (StringUtils.isEmpty(charset)) {
            signTool.update(content.getBytes());
        } else {
            signTool.update(content.getBytes(charset));
        }
        byte[] signBytes = signTool.sign();
        //二进制转十六进制
        return DockHex._2_16(signBytes);
    }

    /**
     * 验证签名
     *
     * @param content   签名的内容
     * @param signature 签名
     * @param algorithm 签名算法
     * @param publicKey 公钥
     * @param charset   当签名内容为中文时，你可能需要指定编码
     * @return 该签名是否和内容匹配
     * @throws NoSuchAlgorithmException     签名算法异常
     * @throws InvalidKeySpecException      密钥格式不正确
     * @throws InvalidKeyException          密钥异常
     * @throws SignatureException           签名异常
     * @throws UnsupportedEncodingException 可能你指定的编码不能正确读取你要签名的内容
     */
    public static boolean verify(String content, String signature, String algorithm, String publicKey, String charset)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        Signature signTool = Signature.getInstance(algorithm);
        PublicKey key = commonGetPublickeyByText(publicKey);
        signTool.initVerify(key);
        if (StringUtils.isEmpty(charset)) {
            signTool.update(content.getBytes());
        } else {
            signTool.update(content.getBytes(charset));
        }
        //十六进制转二进制
        return signTool.verify(DockHex._16_2(signature));
    }

    public static void ras() {
        try {
            String[] key = DockRSA.getKey(512);
            String rsaPrivateKey = key[0];
            String rsaPublicKey = key[1];
            System.out.println("rsaPrivateKey: " + rsaPrivateKey);
            System.out.println("rsaPublicKey: " + rsaPublicKey);
            //签名 SHA256WithRSA
            String sign = DockRSA.sign("签名测试内容", "SHA256WithRSA", rsaPrivateKey, "utf-8");
            System.out.println("私钥签名：" + sign);
            //验签
            boolean verify = DockRSA.verify("签名测试内容", sign, "SHA256WithRSA", rsaPublicKey, "utf-8");
            System.out.println("公钥验签：" + verify);
            //私钥加密
            String privateKeyEncrypt = DockRSA.privateKeyEncrypt(rsaPrivateKey, "私钥加密公钥解密");
            System.out.println("私钥加密：" + privateKeyEncrypt);
            //公钥解密
            String publicKeyDecrypt = DockRSA.publicKeyDecrypt(rsaPublicKey, privateKeyEncrypt);
            System.out.println("公钥解密：" + publicKeyDecrypt);
            //公钥加密
            String publicKeyEncrypt = DockRSA.publicKeyEncrypt(rsaPublicKey, "公钥加密私钥解密");
            System.out.println("公钥加密：" + publicKeyEncrypt);
            //私钥解密
            String privateKeyDecrypt = DockRSA.privateKeyDecrypt(rsaPrivateKey, publicKeyEncrypt);
            System.out.println("私钥解密：" + privateKeyDecrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ras();
    }
}