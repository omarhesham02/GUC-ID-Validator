package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
public class IDValidatorController {

        // Method to check if the ID matches the ID pattern for GUC IDs (XX-XXXXX)
        public static boolean isValidIDPattern (final String input) {
            final Pattern pattern = Pattern.compile("\\d+-\\d+", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        }

    @GetMapping("/validate")
    public IDValidator idValidator(@RequestParam(value = "ID", defaultValue = "None") String ID) {
            ID = ID.replaceAll("//s", ""); // I tried to remove all whitespace from the ID (to account for IDs written as 52 - 8724 for example)
                                                           // but this is not actually working, and I am not sure why
        if (!isValidIDPattern(ID))
            return new IDValidator("Invalid ID"); // Check if the ID matches the ID pattern for GUC IDs (XX-XXXXX)

        // Split the ID into the batch number and the student's unique ID number
        String[] splitID = ID.split("-");
        int batchNumber = Integer.parseInt(splitID[0]);
        int idNumber = Integer.parseInt(splitID[1]);

        // Now that it is known that the ID matches the pattern, it is left to check whether the numbers entered are valid ID numbers
        boolean isValidID = batchNumber >= 1 && batchNumber <= 58 && idNumber >= 0 && idNumber <= 99999;

        // If the ID is a valid one, calculate the year of entry (If it is not, the year of entry is never used, but it is left here as -1)
        int yearOfEntry = isValidID ? (batchNumber - 1) / 3 + 2003 : -1;

        // Generate the string to display as a response
        String template = isValidID ? "Valid ID. Year of entry: " + yearOfEntry : "Invalid ID";

        return new IDValidator(template);
    }
}

