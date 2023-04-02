package com.app.ourchat.utils;

import java.security.MessageDigest;

public class SHA1Util {

    public static String shaEncode(String str) throws Exception{

        System.out.println("原始str："+str);

        MessageDigest sha=null;

        try{

            sha =MessageDigest.getInstance("SHA");

        }catch(Exception e){

            System.out.println(e.toString());

            e.getStackTrace();

            return  "";

        }

        byte[] byteArray =str.getBytes("utf-8");

        byte[] md5Bytes =sha.digest(byteArray);

        StringBuffer hexValue= new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {

            int val=((int)md5Bytes[i])& 0xff;

            if(val<16){

                hexValue.append("0");

            }

            hexValue.append(Integer.toHexString(val));

        }

        System.out.println("加密："+ hexValue.toString());

        return  hexValue.toString();

    }
}
