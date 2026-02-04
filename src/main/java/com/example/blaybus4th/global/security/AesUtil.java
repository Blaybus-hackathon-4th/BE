package com.example.blaybus4th.global.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesUtil {

    @Value("${SCRIPT_AES_ALGORITHM}")
    private String scriptAesAlgorithmValue;

    @Value("${SCRIPT_AES_KEY}")
    private String scriptAesKeyValue;

    private static String scriptAesAlgorithm;
    private static String scriptAesKey;

    @PostConstruct
    void init() {
        scriptAesAlgorithm = scriptAesAlgorithmValue;
        scriptAesKey = scriptAesKeyValue;
    }

    public static String encrypt(String plainText){
        try{
            Cipher cipher = Cipher.getInstance(scriptAesAlgorithm);
            SecretKeySpec keySpec = new SecretKeySpec(scriptAesKey.getBytes(StandardCharsets.UTF_8), "AES");

            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] ivAndEncrypted = new byte[iv.length+encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
            System.arraycopy(encrypted, 0, ivAndEncrypted, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(ivAndEncrypted);

        }catch(Exception e){
            throw new RuntimeException("AES 암호화 실패" + e);
        }
    }

    public static String decrypt(String cipherText){
        try {
            byte[] ivAndEncrypted = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[ivAndEncrypted.length - 16];
            System.arraycopy(ivAndEncrypted, 0, iv, 0, 16);
            System.arraycopy(ivAndEncrypted, 16, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance(scriptAesAlgorithm);
            SecretKeySpec keySpec = new SecretKeySpec(scriptAesKey.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES 복호화 실패", e);
        }
    }

}
