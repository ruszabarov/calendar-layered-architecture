package rockets.data_access_layer.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class Check {

    // RETURNS A STRING
    // Trims the string to the limit
    public static String limitString(String input, int maxLength) {
        // If the input string is longer than the specified limit
        // Example: 2000 characters or 10000 characters
        if (input.length() > maxLength) {
            return input.substring(0, maxLength);
        }
        // Otherwise, return the original string
        return input;
    }

    // Email Checker:
    // Validates that an email is in the proper format
    public static boolean isValidEmail(String email) {
        // DO NOT DELETE
        // NOT SURE HOW IT WORKS but it defines an expression for the email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(emailRegex);

        // Check if the email matches the pattern
        if (email == null) {
            return false;
        }
        // Returns true or false depending on if the email is in proper format
        return pattern.matcher(email).matches();
    }

    // Validates if a URL is valid or not
    public static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}