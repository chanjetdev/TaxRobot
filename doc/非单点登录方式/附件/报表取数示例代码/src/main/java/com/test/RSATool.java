package com.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * 封装同RSA非对称加密算法有关的方法，可用于数字签名，RSA加密解密
 */
public class RSATool {
    public RSATool() {
    }

    /**
     * 使用RSA公钥加密数据
     *
     * @param pubKeyInByte 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用RSA公钥加密数据(字符串形式，返回字符串）
     * @param publicKey
     * @param source
     * @return
     */
    public static String encryptByRSAStr(String publicKey, String source) {
        byte[] btPublicKey=Base64.decodeBase64(publicKey.getBytes());

        String result="";
        try {
            //获得摘要
            byte[] btSource = source.getBytes("UTF-8");
            //使用公钥对摘要进行加密 获得密文
            byte[] signpub_pri =encryptByRSA(btPublicKey ,btSource);
            result=new String(Base64.encodeBase64(signpub_pri));
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    //公钥加密 测试
    public static void main(String[] args) {
        //公钥
        String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTUaxC5BL/J/SaDDBXe26sXNU/LsGvDmDsfHoHGrrPWy2IC+xPUV7Rl7GEoA/f1ei0rn3Kje2SXKxdr2sowPnVp/FvnczvM1lQ5KVe8CcG/F0Bva7VzQfv/nSe7yXM7OhtLF3baLU+pESOxAnEEbNba0Wd/ftypcb5WhzX7e7EHQIDAQAB";

        System.out.println("公钥加密密文:"+ encryptByRSAStr(publicKey,"假设这是要加密的税号1111"));
    }
}