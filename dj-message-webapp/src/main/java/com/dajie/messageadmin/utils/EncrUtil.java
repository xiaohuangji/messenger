package com.dajie.messageadmin.utils;

import java.math.BigInteger;  
 
public class EncrUtil {

    private static final int RADIX = 32;  
    private static final String SEED = "6937343168828";//随手找了串数字。来自慢严舒柠的条形码
  
    public static final String encrypt(String password) {  
        if (password == null)  
            return "";  
        if (password.length() == 0)  
            return "";  
  
        BigInteger bi_passwd = new BigInteger(password.getBytes());  
  
        BigInteger bi_r0 = new BigInteger(SEED);  
        BigInteger bi_r1 = bi_r0.xor(bi_passwd);  
  
        return bi_r1.toString(RADIX);  
    }  
  
    public static final String decrypt(String encrypted) {  
        if (encrypted == null)  
            return "";  
        if (encrypted.length() == 0)  
            return "";  
  
        BigInteger bi_confuse = new BigInteger(SEED);  
  
        try {  
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);  
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);  
  
            return new String(bi_r0.toByteArray());  
        } catch (Exception e) {  
            return "";  
        }  
    }  
//
//    public static void main(String args[]){
//    	String data="{fasfdsafdsafsfsergsafsafds}";
//        System.out.println(EncrUtil.encrypt(data));
//        System.out.println(EncrUtil.decrypt(EncrUtil.encrypt(data)));
//
//
//    }
      
}  
