package com.dajie.message.mcp.passport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aes encryption
 */
public class AES {

	private static Logger logger = LoggerFactory.getLogger(AES.class);
	private static SecretKeySpec secretKey;
	private static byte[] key;

	private static String decryptedString;
	private static String encryptedString;

	public static void setKey(String myKey) {

		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {			
			logger.error(e.getLocalizedMessage(), e);
		} catch (UnsupportedEncodingException e) {			
			logger.error(e.getLocalizedMessage(), e);
		}

	}

	public static String getDecryptedString() {
		return decryptedString;
	}

	public static void setDecryptedString(String decryptedString) {
		AES.decryptedString = decryptedString;
	}

	public static String getEncryptedString() {
		return encryptedString;
	}

	public static void setEncryptedString(String encryptedString) {
		AES.encryptedString = encryptedString;
	}
	
	public static String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt
							.getBytes("UTF-8"))));

		} catch (Exception e) {
			logger.error("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String encryptForURL(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			/** '+'号在浏览器的url里传输会不正确，加密时用URLEncoder **/
			setEncryptedString(URLEncoder.encode(Base64
					.encodeBase64String(cipher.doFinal(strToEncrypt
							.getBytes("UTF-8"))), "UTF-8"));

		} catch (Exception e) {
			logger.error("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			setDecryptedString(new String(cipher.doFinal(Base64
					.decodeBase64(strToDecrypt))));
		} catch (Exception e) {
			logger.error("Error while decrypting: " + e.toString());
		}
		return null;
	}

//	public static void main(String args[]) {
//		final String strToEncrypt = "My text to encrypt";
//		final String strPssword = "encryptor key";
//		AES.setKey(strPssword);
//
//		AES.encrypt(strToEncrypt.trim());
//
//		System.out.println("String to Encrypt: " + strToEncrypt);
//		System.out.println("Encrypted: " + AES.getEncryptedString());
//
//		final String strToDecrypt = AES.getEncryptedString();
//		AES.decrypt(strToDecrypt.trim());
//
//		System.out.println("String To Decrypt : " + strToDecrypt);
//		System.out.println("Decrypted : " + AES.getDecryptedString());
//
//	}

}