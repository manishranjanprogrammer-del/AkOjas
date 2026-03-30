package com.ojassoft.astrosage.utils;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ojas on २९/६/१६.
 */
public class AesHelper {




    public static String encryptFromAES(String seed,String clearText) {
        byte[] encryptedText = null;
        //Log.e("seed",clearText);
        String   seed1=getKey(seed);
        byte[] keyData = seed1.getBytes();
        SecretKey ks = new SecretKeySpec(keyData, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] inputByte = clearText.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, ks);
            return new String (Base64.encode(cipher.doFinal(inputByte), Base64.DEFAULT));










        /*    byte[] keyData = seed1.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(clearText.getBytes("UTF-8"));
      //      return Base64.encodeToString(encryptedText, Base64.DEFAULT);

            return new String (Base64.encode(encryptedText, Base64.DEFAULT));*/

        } catch (Exception e) {
            return null;
        }
    }


    public static String decryptFromAES (String seed,String encryptedText) {
        byte[] clearText = null;
        //Log.e("seed",encryptedText);

        try {

            String   seed1=getKey(seed);
            byte[] keyData = seed1.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] inputByte = encryptedText.getBytes("UTF-8");
            cipher.init(Cipher.DECRYPT_MODE, ks);
            return new String (cipher.doFinal(Base64.decode(inputByte, Base64.DEFAULT)));



        /*    byte[] keyData = seed1.getBytes();
            byte[] inputByte = encryptedText.getBytes("UTF-8");
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(inputByte, Base64.DEFAULT));
            //Log.e("Text",clearText.toString());
            String str=new String(clearText, "UTF-8");
            return str;*/
            //return new String (cipher.doFinal(Base64.decode(inputByte, Base64.DEFAULT)));

        } catch (Exception e) {
            return null;
        }
    }


    private  static String getKey(String keyText)
    {
        String hex = keyText+keyText+keyText+keyText+keyText;
        return hex.substring(0,16);
    }



    






















}
