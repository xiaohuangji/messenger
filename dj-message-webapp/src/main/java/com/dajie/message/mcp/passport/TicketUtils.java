package com.dajie.message.mcp.passport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class TicketUtils {

	    private final static String KEY = "((*!feew12@#%$JU78";
	    
	    private static final String T_VERSION = "B";
	    
	    public static final String ENCODING_TYPE = "UTF-8";

	    public static String generateTicket(int userId) {
	        if (userId <= 0) {
	            return null;
	        }

	        long now = System.currentTimeMillis();
	        String mix = mixContent(T_VERSION + now, userId + "");

	        String ticket = null;
	        try {
	            ticket = T_VERSION + AzDGCrypt.encrypt(mix, KEY);
	            ticket = ticket.replace("\n", "").replace("\r", "").replace("/", "-").replace("+", "_")
	                    .replace("=", ".");
	        } catch (Exception e) {
	           // logger.error("generateTicket", e);
	        }

	        return ticket;
	    }

	    public static int decryptTicket(String ticket) {
	        int userId = 0;
	        if (StringUtils.isEmpty(ticket)) {
	            return userId;
	        }
	        try {
	            ticket = ticket.substring(1);
	            ticket = ticket.replace("-", "/").replace("_", "+").replace(".", "=");
	            String dStr = AzDGCrypt.decrypt(ticket, KEY);
	            userId = getUserIdFromMix(dStr);
	        } catch (Exception e) {
//	            logger.error("decryptTicket", e);
	           /* if (logger.isDebugEnabled()) {
	                logger.debug("decryptTicket", e);
	            }*/
	        }
	        return userId;
	    }

	    private static String mixContent(String maskStr, String uidStr) {
	        int maskIdx = 0;
	        try {
	            byte[] maskBytes = maskStr.getBytes(ENCODING_TYPE);
	            byte[] uidBytes = uidStr.getBytes(ENCODING_TYPE);
	            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	            for (int i = 0; i < uidBytes.length; i++) {
	                maskIdx = maskIdx % maskBytes.length;
	                byteOut.write(uidBytes[i]);
	                byteOut.write(maskBytes[maskIdx++]);
	            }
	            return new String(byteOut.toByteArray(), ENCODING_TYPE);
	        } catch (UnsupportedEncodingException e) {
	           /* logger.error("", e);*/
	        }
	        
	        return null;
	    }
	    
	    private static int getUserIdFromMix(String mix) {
	        try {
	            byte[] mixBytes = mix.getBytes(ENCODING_TYPE);
	            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	            for (int i = 0; i < mixBytes.length; i++) {
	                if (i % 2 == 0) {
	                    byteOut.write(mixBytes[i]);
	                }
	            }
	            return NumberUtils.toInt(new String(byteOut.toByteArray(), ENCODING_TYPE));
	        } catch (UnsupportedEncodingException e) {
	           /* logger.error("", e);*/
	        }
	        
	        return 0;
	    }
	    
	    public static byte[] serialize(Object object) {
	        ObjectOutputStream oos = null;
	        ByteArrayOutputStream baos = null;
	        try {
	            //序列化
	            baos = new ByteArrayOutputStream();
	            oos = new ObjectOutputStream(baos);
	            oos.writeObject(object);
	            return baos.toByteArray();
	        } catch (Exception e) {
	           /* logger.error("serialize", e);*/
	        } finally {
	            try {
	                oos.close();
	            } catch (IOException e) {
	               /* logger.error("serialize", e);*/
	            }
	        }
	        return null;
	    }

	    public static Object unserialize(byte[] bytes) {
	        ByteArrayInputStream bais = null;
	        try {
	            //反序列化
	            bais = new ByteArrayInputStream(bytes);
	            ObjectInputStream ois = new ObjectInputStream(bais);
	            return ois.readObject();
	        } catch (Exception e) {
	           /* logger.error("unserialize", e);*/
	        } finally {
	            try {
	                bais.close();
	            } catch (IOException e) {
	               /* logger.error("unserialize", e);*/
	            }
	        }
	        return null;
	    }

	    public static void main(String[] s) throws IOException, InvalidKeyException,
	            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
	            BadPaddingException {
	    	//System.out.println(decryptTicket("BAGQHFgw9CzFRYgFhDjVUPgw7ATFQY1E5WzFbZlVlBmA."));;
//	        long time = System.currentTimeMillis();
//	        String t = generateTicket(324265734);
//	        System.out.println(t + " cost:" + (System.currentTimeMillis() - time));
//	        time = System.currentTimeMillis();
//	        int uid = decryptTicket(t);
//	        //        int uid = decryptTicket("K-nXSSav6he3oqYRVu7tRf9685btSiavEGDOEUwTZLU=");
//	        System.out.println(uid + " cost:" + (System.currentTimeMillis() - time));
	        
//	        System.out.println("start");
//	        int times = 100000;
//	        for (int i = 0; i < times; i++) {
//	            int uid = RandomUtils.nextInt();
//	            String t = generateTicket(uid);
//	            System.out.println("------------"+t);
//	            int uidRt = decryptTicket(t);
//	            if (uid != uidRt) {
//	                System.out.println(uid + "!=" + uidRt);
//	            }
//	            
//	        }
//	        System.out.println("end");

	    }
}
