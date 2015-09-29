package com.agois;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar hasher.jar <android or ios> <input file>");
            System.exit(-1);
        }
        String platform = args[0];
        String inputFilename = args[1];
        System.out.println("Running for platform " + platform);
        System.out.println("Input file is " + inputFilename);

        int extensionIndex = inputFilename.lastIndexOf(".");
        String outputFilename = (extensionIndex != -1 ? inputFilename.substring(0, extensionIndex) : inputFilename) + "-hashed.csv";

        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(inputFilename));
        } catch (FileNotFoundException e) {
            System.out.println("Unknown file: " + inputFilename);
            System.exit(-1);
        }

        BufferedWriter outputStream = null;
        try {
            outputStream = new BufferedWriter(new FileWriter(outputFilename));
        } catch (IOException e) {
            System.out.println("Couldn't open file: " + inputFilename);
            System.exit(-1);
        }

        String line;
        try {
            TextHasher hasher = new TextHasher();
            boolean isIOS = "ios".equalsIgnoreCase(platform);
            while ((line = inputStream.readLine()) != null) {
                String hashed = hasher.createHashedString(line);
                outputStream.write("alias," + (isIOS ? hashed.toUpperCase() : hashed));
                outputStream.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFilename);
            System.exit(-1);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Success! Output is in file " + outputFilename);
    }

     private static class TextHasher {

         private final StringBuffer mHashedBuffer = new StringBuffer(64);

         private String createHashedString(String from) {
             mHashedBuffer.delete(0, mHashedBuffer.length());

             try {
                 MessageDigest digest = MessageDigest.getInstance("MD5");
                 digest.reset();
                 byte[] data = digest.digest(from.getBytes());

                 for (byte aData : data) {
                     String h = Integer.toHexString(0xFF & aData);
                     if (h.length() < 2) {
                         mHashedBuffer.append("0");
                     }
                     mHashedBuffer.append(h);
                 }
             } catch (NoSuchAlgorithmException e1) {
                 e1.printStackTrace();
             }

             return mHashedBuffer.toString();
         }
     }
}
