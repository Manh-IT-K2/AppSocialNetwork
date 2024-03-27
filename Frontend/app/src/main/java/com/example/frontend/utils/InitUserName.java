package com.example.frontend.utils;

import java.text.Normalizer;
import java.util.List;

public class InitUserName {
    private static List<String> existingNames;
    private static String fullName;

    // Không cần constructor

    public static List<String> getExistingNames() {
        return existingNames;
    }

    public static void setExistingNames(List<String> existingNames) {
        InitUserName.existingNames = existingNames;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        InitUserName.fullName = fullName;
    }

    public static String formatName() {
        // Normalize and remove Vietnamese accents
        String normalized = removeVietnameseAccent(fullName.toLowerCase());

        // Split the string into words
        String[] words = normalized.split("\\s+");

        // Combine the last two words
        String lastName = words[words.length - 2] + "_" + words[words.length - 1];

        // If the name already exists, add a number suffix to it
        String formattedName = lastName;
        int counter = 1;
        while (existingNames.contains(formattedName)) {
            formattedName = lastName + "_" + counter;
            counter++;
        }

        // Add the formatted name to the list of existing names
        existingNames.add(formattedName);

        return formattedName;
    }

    private static String removeVietnameseAccent(String str) {
        // Phương thức removeVietnameseAccent không cần thiết phải là public, nên nên đặt là private
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
