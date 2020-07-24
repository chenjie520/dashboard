package com.net.dashboard.util;/**
 * @Author chen_jie
 * @Date 2020/7/24 ${Time}
 * @Version 1.0
 * @Description:
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/24
 **/
public class Md5Utils {
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static void main(String[] args) {
        System.out.println("93279e3308bdbbeed946fc965017f67a");
        System.out.println(Md5Utils.stringToMD5("121212"));
    }
}
