package com.example.urlshortner.utils;

public class shortcodegenerator {
    
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORTCODE_LENGTH = 6;
    private static final java.util.Random RANDOM = new java.util.Random();

    public shortcodegenerator() {
    }
    
    public static String generateShortcode() {
        StringBuilder shortcode = new StringBuilder(SHORTCODE_LENGTH);
        for (int i = 0; i < SHORTCODE_LENGTH; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            shortcode.append(ALPHABET.charAt(index));
        }
        return shortcode.toString();
    }
}
