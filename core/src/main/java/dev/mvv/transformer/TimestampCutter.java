package dev.mvv.transformer;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.UnaryOperator;

public class TimestampCutter implements UnaryOperator<String> {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String apply(String s) {
        var firstSpace = s.indexOf(' ');
        if (firstSpace <= 0) {
            return s;
        }
        var secondSpace = s.indexOf(' ', firstSpace + 1);
        if (secondSpace < 0) {
            return s;
        }

        try {
            TIMESTAMP_FORMATTER.parse(s.substring(0, secondSpace));
            return s.substring(secondSpace);
        } catch (DateTimeParseException e) {
            return s;
        }
    }
}
