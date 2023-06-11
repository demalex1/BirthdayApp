package com.mirea.kt.android2023.birthdayapp.calculator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class Calculator {

    public static String calculateAge(LocalDate birthday) {
        LocalDate localDate = LocalDate.now();

        Period period = Period.between(birthday, localDate);

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        String yearUnit;
        if (years < 5) {
            yearUnit = "г.";
        } else {
            yearUnit = "л.";
        }

        LocalDateTime time = LocalDateTime.now();

        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();

        return years + " " + yearUnit + " " + months + " мес. " + days + " дн. " + hours + " ч. " + minutes + " мин. " + seconds + " с.";
    }

    public static String calculateDaysToBirthday(LocalDate birthday) {
        LocalDate localDate = LocalDate.now();

        LocalDate nextBirthday = LocalDate.of(localDate.getYear(), birthday.getMonth(), birthday.getDayOfMonth());

        if (nextBirthday.isBefore(localDate)) {
            nextBirthday = LocalDate.of(localDate.getYear() + 1, birthday.getMonth(), birthday.getDayOfMonth());
        }

        Period period = Period.between(localDate, nextBirthday);

        int months = period.getMonths();
        int days = period.getDays();

        if (days == 0 && months == 0) {
            return "birthday";
        }

        if (months == 0) {
            return days + " дн.";
        }

        if (days == 0) {
            return months + " мес.";
        }

        return months + " мес. " + days + " дн.";
    }
}
