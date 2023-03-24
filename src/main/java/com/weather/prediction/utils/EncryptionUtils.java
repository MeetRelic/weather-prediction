package com.weather.prediction.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtils {

    private static SecretKeySpec secretKeySpec;
    private static byte[] key;

    private EncryptionUtils() {
    }

    public static void setKey(String secretKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        key = secretKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String secretKey, String input) {
        try {
            setKey(secretKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
        } catch (Exception e) {

        }
        return null;
    }

    public static String decrypt(String secretKey, String input) {
        try {
            setKey(secretKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
