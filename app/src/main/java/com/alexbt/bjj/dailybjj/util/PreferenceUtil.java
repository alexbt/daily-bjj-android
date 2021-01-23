package com.alexbt.bjj.dailybjj.util;

import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PreferenceUtil {

    public static int getScheduledNotificationHours(SharedPreferences sharedPreferences) {
        int hours = sharedPreferences.getInt("scheduled_notification_hours", 8);
        return hours;
    }

    public static LocalDateTime getLastNotification(SharedPreferences sharedPreferences) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String lastNotificationTimeStr = sharedPreferences.getString("last_notification_time", null);
        if (lastNotificationTimeStr != null) {
            return LocalDateTime.parse(lastNotificationTimeStr);
        }
        return null;
    }

    public static int getScheduledNotificationMinutes(SharedPreferences sharedPreferences) {
        int minutes = sharedPreferences.getInt("scheduled_notification_minutes", 0);
        return minutes;
    }

    public static void saveNotificationTime(SharedPreferences sharedPreferences, int hour, int minutes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("scheduled_notification_hours", hour);
        editor.putInt("scheduled_notification_minutes", minutes);
        editor.commit();

    }

    public static String getNextNotificationText(SharedPreferences sharedPreferences) {
        int hours = PreferenceUtil.getScheduledNotificationHours(sharedPreferences);
        int minutes = PreferenceUtil.getScheduledNotificationMinutes(sharedPreferences);
        LocalDateTime lastNotification = PreferenceUtil.getLastNotification(sharedPreferences);
        String day = "Today";

        if (lastNotification.getDayOfYear() == LocalDate.now().getDayOfYear()) {
            day = "Tomorrow";
        }

        return String.format("%s at %s", day, formatTime(hours, minutes));
    }

    public static String getLastNotificationText(SharedPreferences sharedPreferences) {
        LocalDateTime lastNotification = PreferenceUtil.getLastNotification(sharedPreferences);

        String day;
        int nbDays = LocalDate.now().getDayOfYear() - lastNotification.getDayOfYear();
        if (lastNotification.getDayOfYear() == LocalDate.now().getDayOfYear()) {
            day = "Today";
        } else if (nbDays == 1) {
            day = "Yesterday";
        } else {
            day = String.format("%d days ago", nbDays);
        }

        return String.format("%s at %s", day, formatTime(lastNotification.getHour(), lastNotification.getMinute()));
    }

    private static String getAmPm(int hours) {
        return hours <= 11 ? "AM" : "PM";
    }

    public static String getScheduledNotificationText(SharedPreferences sharedPreferences) {
        int hours = PreferenceUtil.getScheduledNotificationHours(sharedPreferences);
        int minutes = PreferenceUtil.getScheduledNotificationMinutes(sharedPreferences);
        return formatTime(hours, minutes);
    }

    public static String formatTime(int hours, int minutes) {
        return String.format("%02dh%02d %s", hours, minutes, getAmPm(hours));
    }

}
