package com.monke.monkeybook.utils.aes;

import com.monke.monkeybook.utils.ParseSystemUtil;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;

public class AESUtil {
    public static String aesEncode(String cleartext, String seed) throws Exception {
        byte[] rawKey = deriveKeyInsecurely(seed, 32).getEncoded();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return ParseSystemUtil.parseByte2HexStr(result);
    }

    public static String aesDecode(String encrypted, String seed) throws Exception {
        byte[] rawKey = deriveKeyInsecurely(seed, 32).getEncoded();
        byte[] enc = ParseSystemUtil.parseHexStr2Byte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static SecretKey deriveKeyInsecurely(String password, int keySizeInBytes) throws UnsupportedEncodingException {
        byte[] passwordBytes = password.getBytes("UTF-8");
        return new SecretKeySpec(
                InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(
                        passwordBytes, keySizeInBytes),
                "AES");
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}