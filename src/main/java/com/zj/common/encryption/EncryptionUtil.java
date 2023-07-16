package com.zj.common.encryption;

import com.zj.common.exception.ResultCode;
import com.zj.common.exception.ValidateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static com.zj.common.exception.ResultCode.ENCRYPTION_UTIL_SHA256_NOT_SUPPORT_EXCEPTION;

/**
 * 加密解密工具类，提供常用的加密解密方法
 */
@Slf4j
public class EncryptionUtil {

    /**
     * 对称加密算法 AES 加密
     *
     * @param plaintext 明文
     * @param password  密码
     * @return 加密后的密文
     */
    public static String encryptWithAES(String plaintext, String password) {
        final Cipher cipher = getAESCipher();
        final SecretKey secretKey = generateAESKey(password);
        byte[] encryptedBytes = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("EncryptionUtil######encryptWithAES 加密异常！", e);
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 对称加密算法AES解密
     *
     * @param ciphertext 密文
     * @param password   密码
     * @return 解密后的明文
     */
    public static String decryptWithAES(String ciphertext, String password) {
        Cipher cipher = getAESCipher();
        SecretKey secretKey = generateAESKey(password);
        byte[] decryptedBytes = new byte[0];
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (Exception e) {
            log.error("EncryptionUtil######decryptWithAES 解密异常！", e);
        }
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 非对称加密算法RSA加密
     *
     * @param plaintext 明文
     * @return 加密后的密文
     */
    public static String encryptWithRSA(String plaintext) {
        Cipher cipher = getRSACipher();
        KeyPair keyPair = generateRSAKeyPair();
        byte[] encryptedBytes = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
            encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("EncryptionUtil######encryptWithRSA 加密异常！", e);
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 非对称加密算法RSA解密
     *
     * @param ciphertext 密文
     * @return 解密后的明文
     */
    public static String decryptWithRSA(String ciphertext) {
        Cipher cipher = getRSACipher();
        KeyPair keyPair = generateRSAKeyPair();
        byte[] decryptedBytes = new byte[0];
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (Exception e) {
            log.error("EncryptionUtil######decryptWithRSA 解密异常！", e);
        }
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 哈希算法 SHA-256
     *
     * @param plaintext 明文
     * @return 哈希值
     */
    public static String hashWithSHA256(String plaintext) {
        MessageDigest digest = getSha256Digest();
        byte[] hashBytes = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    public static MessageDigest getSha256Digest() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.warn("EncryptionUtil######hashWithSHA256 哈希算法不支持异常 获取 SHA-256 的 MessageDigest 获取实例失败！", e);
        }
        ValidateUtil.requireNonNull(digest, ENCRYPTION_UTIL_SHA256_NOT_SUPPORT_EXCEPTION);
        return digest;
    }

    public static KeyGenerator getAESKeyGenerator() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            log.warn("EncryptionUtil######generateAESKey 密钥生成算法不支持异常 获取 AES 的 KeyGenerator 获取实例失败！", e);
        }
        ValidateUtil.requireNonNull(keyGenerator, ResultCode.ENCRYPTION_UTIL_ASE_NOT_SUPPORT_EXCEPTION);
        return keyGenerator;
    }

    public static Cipher getRSACipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (Exception e) {
            log.warn("EncryptionUtil######getRSACipher 密钥生成算法不支持异常 获取 RSA 的 Cipher 获取实例失败！", e);
        }
        ValidateUtil.requireNonNull(cipher, ResultCode.ENCRYPTION_UTIL_RSA_NOT_SUPPORT_EXCEPTION);
        return cipher;
    }

    public static Cipher getAESCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            log.warn("EncryptionUtil######getAESCipher 密钥生成算法不支持异常 获取 AES 的 Cipher 获取实例失败！", e);
        }
        ValidateUtil.requireNonNull(cipher, ResultCode.ENCRYPTION_UTIL_ASE_NOT_SUPPORT_EXCEPTION);
        return cipher;
    }

    public static KeyPairGenerator getRSAKeyPairGenerator() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (Exception e) {
            log.warn("EncryptionUtil######getRSAKeyPairGenerator 密钥生成算法不支持异常 获取 RSA 的 KeyPairGenerator 获取实例失败！", e);
        }
        ValidateUtil.requireNonNull(keyPairGenerator, ResultCode.ENCRYPTION_UTIL_RSA_KEY_PAIR_NOT_SUPPORT_EXCEPTION);
        return keyPairGenerator;
    }

    /**
     * 生成AES密钥
     *
     * @param password 密码
     * @return 密钥
     */
    private static SecretKey generateAESKey(String password) {
        KeyGenerator keyGenerator = getAESKeyGenerator();
        keyGenerator.init(128);
        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        final MessageDigest digest = getSha256Digest();
        final byte[] keyBytes = digest.digest(passwordBytes);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 生成RSA密钥对
     *
     * @return 密钥对
     */
    private static KeyPair generateRSAKeyPair() {
        final KeyPairGenerator keyPairGenerator = getRSAKeyPairGenerator();
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

    public static void main(String[] args) {
        String pwd = "123455";

        String encryptWithRSA = encryptWithRSA(pwd);
        String decryptWithRSA = decryptWithRSA(pwd);

        System.out.println("pwd=" + pwd + " encryptWithRSA=" + encryptWithRSA);
        System.out.println("pwd=" + pwd + " decryptWithRSA=" + decryptWithRSA);

    }
}
