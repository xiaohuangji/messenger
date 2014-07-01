package com.dajie.push.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class MsgAzDGCrypt {

	    public static final String ENCODING_TYPE = "UTF-8";
	    
	    /**
	     * encrypt
	     * @param txt
	     * @param key
	     * @return
	     * @throws java.io.UnsupportedEncodingException
	     */
	    private static byte[] encrypt(byte[] txt, byte[] key) throws UnsupportedEncodingException {
	        int rand = new Double(Math.random() * 32000).intValue();
	        byte[] encrypt_key = DigestUtils.md5Hex(rand + "").getBytes(ENCODING_TYPE);

	        byte ctr = 0;
	        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	        for (int i = 0; i < txt.length; i++) {
	            ctr = ctr == encrypt_key.length ? 0 : ctr;
	            byteOut.write(encrypt_key[ctr]);
	            byteOut.write(txt[i] ^ encrypt_key[ctr++]);
	        }

	        return Base64.encodeBase64String(encodeKey(byteOut.toByteArray(), key)).getBytes(ENCODING_TYPE);
	    }

	    public static String encrypt(String txt, String key) {
	        try {
	            return new String(encrypt(txt.getBytes(ENCODING_TYPE), key.getBytes(ENCODING_TYPE)));
	        } catch (UnsupportedEncodingException e) {
	            return null;
	        }
	    }

        /**
         *
         * @param txt
         * @param encrypt_key
         * @return
         * @throws java.io.UnsupportedEncodingException
         */
        private static byte[] encodeKey(byte[] txt, byte[] encrypt_key) throws UnsupportedEncodingException {

            encrypt_key = DigestUtils.md5Hex(new String(encrypt_key)).getBytes(ENCODING_TYPE);

            byte ctr = 0;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            for (int i = 0; i < txt.length; i++) {
                ctr = ctr == encrypt_key.length ? 0 : ctr;
                byteOut.write(txt[i] ^ encrypt_key[ctr++]);
            }
            return byteOut.toByteArray();
        }

	    /**
	     * decrypt
	     *
	     * @param txt
	     * @param key
	     * @return
	     * @throws java.io.UnsupportedEncodingException
	     */
	    private static byte[] decrypt(byte[] txt, byte[] key) throws UnsupportedEncodingException {
	        txt = encodeKey(Base64.decodeBase64(txt), key);
	        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	        for (int i = 0; i < txt.length; i++) {
	            byte md5 = txt[i];
	            byteOut.write(txt[++i] ^ md5);
	        }
	        return byteOut.toByteArray();
	    }

	    /**
	     *
	     * @param txt
	     * @param key
	     * @return
	     */
	    public static String decrypt(String txt, String key) {
	        try {
	            return new String(decrypt(txt.getBytes(ENCODING_TYPE), key.getBytes(ENCODING_TYPE)));
	        } catch (UnsupportedEncodingException e) {
	            return null;
	        }
	    }


//    public static void main(String[] args) {
//        String s="i love china";
//        String key="oyNQtykh1oDizQXrf5jnKbzn";
//        String e=MsgAzDGCrypt.encrypt(s, key);
//        System.out.println(e);
//        System.out.println(MsgAzDGCrypt.decrypt(e,key));
//    }
}
