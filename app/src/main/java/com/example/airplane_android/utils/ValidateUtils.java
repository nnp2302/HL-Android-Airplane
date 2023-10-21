package com.example.airplane_android.utils;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidateUtils {
    public static boolean checkEmail(String email) {
        final String regex = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public static boolean checkPassword(String password) {
        final String regex = "^(?=.*[A-Z]).{8,}$";
        return Pattern.matches(regex, password);
    }

    public static boolean checkRegisterPasswordMatch(String password, String rePassword) {
        return password.equals(rePassword);
    }

    public static boolean checkPhoneNumber(String phone) {
        final String regex = "\\b(0\\d{9,10}|0\\d{3}-\\d{3}-\\d{4})\\b";
        return Pattern.matches(regex, phone);
    }

    public static boolean checkBirthDay(String birth) {
        String[] birthSplit = birth.split("/");
        int birthOfYear = Integer.parseInt(birthSplit[2]);
        Calendar calendar = Calendar.getInstance();
        int[] currentYear = {calendar.get(Calendar.YEAR)};

        return (currentYear[0] - birthOfYear) < 16;
    }
}
