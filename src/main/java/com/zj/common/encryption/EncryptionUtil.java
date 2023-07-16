package com.zj.common.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密解密工具类，提供常用的加密解密方法
 */
public class EncryptionUtil {

    /**
     * 对称加密算法 AES 加密
     *
     * @param plaintext 明文
     * @param password  密码
     * @return 加密后的密文
     * @throws Exception 加密异常
     */
    public static String encryptWithAES(String plaintext, String password) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES");
        final SecretKey secretKey = generateAESKey(password);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 对称加密算法AES解密
     *
     * @param ciphertext 密文
     * @param password   密码
     * @return 解密后的明文
     * @throws Exception 解密异常
     */
    public static String decryptWithAES(String ciphertext, String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secretKey = generateAESKey(password);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 非对称加密算法RSA加密
     *
     * @param plaintext 明文
     * @return 加密后的密文
     * @throws Exception 加密异常
     */
    public static String encryptWithRSA(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPair keyPair = generateRSAKeyPair();
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
    

 * 非对称加密算法RSA解密
     *
     * @param ciphertext 密文
     * @return 解密后的明文
     * @throws Exception 解密异常
     */
    public static String decryptWithRSA(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPair keyPair = generateRSAKeyPair();
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 哈希算法 SHA-256
     *
     * @param plaintext 明文
     * @return 哈希值
     * @throws NoSuchAlgorithmException 哈希算法不支持异常
     */
    public static String hashWithSHA256(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    /**
     * 生成AES密钥
     *
     * @param password 密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 密钥生成算法不支持异常
     */
    private static SecretKey generateAESKey(String password) throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] keyBytes = digest.digest(passwordBytes);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 生成RSA密钥对
     *
     * @return 密钥对
     * @throws NoSuchAlgorithmException 密钥生成算法不支持异常
     */
    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            final String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    // Base64 编码
    public static String encodeBase64(String plainText) {
        final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(plainBytes);
    }

    // Base64 解码
    public static String decodeBase64(String base64Text) {
        final byte[] base64Bytes = Base64.getDecoder().decode(base64Text);
        return new String(base64Bytes, StandardCharsets.UTF_8);
    }

}
