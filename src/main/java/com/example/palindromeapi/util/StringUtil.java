package com.example.palindromeapi.util;

public class StringUtil {

    public static String removeNumbersAndSpecialChars(String str) {
        // Remove numbers and special characters from the input string
        return str.replaceAll("[^a-zA-Z]", "");
    }

    
}