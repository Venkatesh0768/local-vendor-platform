package org.localvendor.authservice.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    /**
     * Generate a secure 6-digit OTP
     */
    public static String generate() {
        int otp = random.nextInt(900000) + 100000; // 6-digit number
        return String.valueOf(otp);
    }

    /**
     * Generate OTP with custom length
     */
    public static String generate(int length) {
        if (length < 4 || length > 10) {
            throw new IllegalArgumentException("OTP length must be between 4 and 10");
        }

        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}