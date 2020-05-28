package com.example.hamrobookmark.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DateTimeParser {
    public static String getTimeFrom(String zuluTime){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long currentTime = System.currentTimeMillis();
            long givenTime = Objects.requireNonNull(sdf.parse(zuluTime)).getTime();
            long diff = (currentTime - givenTime) / 1000;
            StringBuilder timeSince = new StringBuilder();
            if (diff <= 10) {
                timeSince.append("few seconds ago");
            } else if (diff < 60) {
                timeSince.append(diff);
                timeSince.append(" seconds ago");
            } else if (diff < 60 * 60) {
                timeSince.append(diff / 60);
                timeSince.append(" minutes ago");
            } else if (diff < 60 * 60 * 24) {
                timeSince.append(diff / (60 * 60));
                timeSince.append(" hours ago");
            } else if (diff < 60 * 60 * 24 * 30) {
                timeSince.append(diff / (60 * 60 * 24));
                timeSince.append(" days ago");
            } else if (diff < 60 * 60 * 24 * 30 * 12) {
                timeSince.append(diff / (60 * 60 * 24 * 30));
                timeSince.append(" months ago");
            } else {
                timeSince.append(diff / (60 * 60 * 24 * 30 * 12));
                timeSince.append(" years ago");
            }
            return timeSince.toString();
        }
        catch(ParseException ex){
            return "n/a";
        }
    }
}
