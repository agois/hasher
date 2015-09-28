package com.asda;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hash of alex.gois@gmail.com is "+hashString("alex.gois@gmail.com"));
    }

     private static String hashString(String text) {
         return text == null ? "" : new StringHasher("MD5").createHashedString(text);
     }

     private static class StringHasher {

         private final String mAlgorithm;
         private final StringBuffer mBuffer = new StringBuffer(64);

         public StringHasher(String algorithm) {
             mAlgorithm = algorithm;
         }

         public String createHashedString(String from) {
             mBuffer.delete(0, mBuffer.length());

             try {
                 MessageDigest digest = MessageDigest.getInstance(mAlgorithm);
                 digest.reset();
                 byte[] data = digest.digest(from.getBytes());

                 // Create Hex String
                 for (byte aData : data) {
                     String h = Integer.toHexString(0xFF & aData);
                     if (h.length() < 2) {
                         mBuffer.append("0");
                     }
                     mBuffer.append(h);
                 }
             } catch (NoSuchAlgorithmException e1) {
                 e1.printStackTrace();
             }

             return mBuffer.toString();
         }
     }
}
