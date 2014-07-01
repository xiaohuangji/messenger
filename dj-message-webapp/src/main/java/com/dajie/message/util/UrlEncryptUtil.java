package com.dajie.message.util;

import com.dajie.message.constants.GoudaConstant;
import com.dajie.message.mcp.passport.AzDGCrypt;

/**
 * Created by wills on 6/9/14.
 */
public class UrlEncryptUtil {

    /**
     * 加密
     * @param url
     * @return
     */
    public static String encrypt(String url){
        String azdg=  AzDGCrypt.encrypt(url, GoudaConstant.AzDG_SECRETKEY);
        //将base64中的/转义成-
        return azdg.replace("/","-");
    }

    /**
     * 解密
     * @param secretUrl
     * @return
     */
    public static String decrypt(String secretUrl){
        //将-转化为/
        String  base64=secretUrl.replace("-","/");
        return AzDGCrypt.decrypt(base64,GoudaConstant.AzDG_SECRETKEY);
    }

//    public static void main(String[] args) {
//        String s="A2MLYAU1A2MNaV44U2sBNgZ-V2VXNlM1AztUMAA3ATMBZAl+UWIDYwBkUjQEPVVmBWA=";
//        System.out.println(UrlEncryptUtil.decrypt(s));
//    }

}
