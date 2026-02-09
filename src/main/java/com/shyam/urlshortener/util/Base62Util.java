package com.shyam.urlshortener.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Base62Util {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(String input, int length) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            BigInteger number = new BigInteger(1, hash);
            StringBuilder sb = new StringBuilder();

            while(number.compareTo(BigInteger.ZERO) > 0 && sb.length() < length) {
                sb.append(BASE62.charAt(number.mod(BigInteger.valueOf(62)).intValue()));
                number = number.divide(BigInteger.valueOf(62));
            }

            return sb.reverse().toString();
        }
        catch(Exception e) {
            throw new RuntimeException("Failed to generate short code", e);
        }
    }
}
