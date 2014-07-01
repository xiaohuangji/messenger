package com.dajie.message.mcp.passport;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class AzDGCrypt {
	// private static final Log logger = LogFactory.getLog(AzDGCrypt.class);
	    
	    public static final String ENCODING_TYPE = "UTF-8";
	    
	    /**
	     * encrypt
	     * 
	     * @param txt
	     * @param key
	     * @return
	     * @throws UnsupportedEncodingException 
	     */
	    public static byte[] encrypt(byte[] txt, byte[] key) throws UnsupportedEncodingException {
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
	           // logger.error("", e);
	            return null;
	        }
	    }

//	    public static String encrypt(String txt, String key, String encoding) {
//	        String str = null;
//	        try {
//	            str = new String(encrypt(txt.getBytes(encoding), key.getBytes(encoding)));
//	        } catch (UnsupportedEncodingException e) {
//	            e.printStackTrace();
//	        }
//	        return str;
//	    }

	    /**
	     * decrypt
	     * 
	     * @param txt
	     * @param key
	     * @return
	     * @throws UnsupportedEncodingException 
	     */
	    public static byte[] decrypt(byte[] txt, byte[] key) throws UnsupportedEncodingException {
	        txt = encodeKey(Base64.decodeBase64(txt), key);
//	        txt = encodeKey(Base64.decodeBase64(Arrays.copyOfRange(txt, 0, txt.length)), key);

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
	            //logger.error("", e);
	            return null;
	        }
	    }
	    
//	    public static String decrypt(String txt, String key, String encoding) {
//	        String str = null;
//	        try {
//	            str = new String(decrypt(txt.getBytes(encoding), key.getBytes(encoding)));
//	        } catch (UnsupportedEncodingException e) {
//	            e.printStackTrace();
//	        }
//	        return str;
//	    }

	    /**
	     * 
	     * @param txt
	     * @param key
	     * @return
	     * @throws UnsupportedEncodingException 
	     */
	    public static byte[] encodeKey(byte[] txt, byte[] encrypt_key) throws UnsupportedEncodingException {

	        encrypt_key = DigestUtils.md5Hex(new String(encrypt_key)).getBytes(ENCODING_TYPE);
	        
	        byte ctr = 0;
//	        byte[] tmp = new byte[txt.length];
	        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	        for (int i = 0; i < txt.length; i++) {
	            ctr = ctr == encrypt_key.length ? 0 : ctr;
//	            tmp[i] = (byte) (txt[i] ^ encrypt_key[ctr++]);
	            byteOut.write(txt[i] ^ encrypt_key[ctr++]);
	        }
	        return byteOut.toByteArray();
	    }

	    public static void main(String[] arg) {
	      /*  String source = "-1";
	        String key = "FASF@W#@$^$@#612sdf";
*/
	       // System.out.println("Source : " + source + " Key : " + key);
	/*        long time = System.currentTimeMillis();
	        String encryptTxt = AzDGCrypt.encrypt(source, key);
	        System.out.println(System.currentTimeMillis() - time);
	        System.out.println("Encypt String : " + encryptTxt);
	        time = System.currentTimeMillis();
	        String decryptTxt = AzDGCrypt.decrypt(encryptTxt, key);
	        System.out.println(System.currentTimeMillis() - time);
	        System.out.println("Decypt String : " + decryptTxt);*/
	        
	    }
}
