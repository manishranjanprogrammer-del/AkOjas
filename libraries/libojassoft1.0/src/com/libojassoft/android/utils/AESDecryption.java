package com.libojassoft.android.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.net.URLDecoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESDecryption {

    public static final int GCM_TAG_LENGTH = 16;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decrypt(byte[] cipherText, SecretKey key, byte[] IV) throws Exception {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decryptFBReferrer(String referrer){
        try {
            String key = LibCGlobalVariables.FACEBOOK_ADS_KEY;
            String decodedRefData = URLDecoder.decode(referrer,"UTF-8");
            decodedRefData = decodedRefData.substring(decodedRefData.lastIndexOf("=")+1);
            String cipher = new JSONObject(decodedRefData).getJSONObject("source").getString("data");
            String nonce = new JSONObject(decodedRefData).getJSONObject("source").getString("nonce");

            byte[] bNonce = AESDecryption.hexToBytes(nonce);
            byte[] bKey = AESDecryption.hexToBytes(key);
            byte[] bCipher = AESDecryption.hexToBytes(cipher);

            SecretKey originalKey = new SecretKeySpec(bKey, 0, bKey.length, "AES");

            return decrypt(bCipher, originalKey ,bNonce);

        } catch (Exception e){
            Log.d("TestRef","Exception --> "+e);
            return "FBEXP="+e;
        }
    }

}
