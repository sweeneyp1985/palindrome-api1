package com.example.palindromeapi;

import com.example.palindromeapi.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class PalindromeController {

    @Value("${palindrome.cache.file.path}")
    private String palindromeCacheFilePath;

    private static final String FILE_CACHE_NAME = "fileContentCache";

    @RequestMapping(value = "/check-palindrome", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkPalindrome(
            @RequestParam("employeeId") String employeeId,
            @RequestParam("inputString") String inputString, Model model) {

        String cleanInput = StringUtil.removeNumbersAndSpecialChars(inputString);
        boolean isPalindrome = isPalindrome(cleanInput);

        // Write to the cache file
        boolean writeSuccess = writeToCacheFile(employeeId, cleanInput, isPalindrome);

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("inputString", cleanInput);
        model.addAttribute("isPalindrome", isPalindrome);
        model.addAttribute("writeSuccess", writeSuccess);

        return "palindromeResult";
    }

    @RequestMapping(value = "/read-file", method = {RequestMethod.GET, RequestMethod.POST})
    public String readFileContent(Model model) {
        String fileContent = readFromFile(palindromeCacheFilePath);

        model.addAttribute("fileContent", fileContent);
        return "fileContent";
    }

    @Cacheable(value = FILE_CACHE_NAME, key = "#palindromeCacheFilePath")
    public boolean writeToCacheFile(String employeeId, String inputString, boolean isPalindrome) {
        // how to write to cache file
        String content = String.format("Employee ID: %s\nInput String: %s\nIs Palindrome: %s\n\n", employeeId, inputString, isPalindrome);
        try {
            Files.write(Paths.get(palindromeCacheFilePath), content.getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            return true; // test if succesful
        } catch (IOException e) {
            e.printStackTrace(); //catch for failures
            return false; // Writing failed
        }
    }

    private boolean isPalindrome(String str) {
        int length = str.length();
        for (int i = 0; i < length / 2; i++) {
            if (str.charAt(i) != str.charAt(length - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private String readFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace(); // catch for failures
            return "Error reading file";
        }
    }
}