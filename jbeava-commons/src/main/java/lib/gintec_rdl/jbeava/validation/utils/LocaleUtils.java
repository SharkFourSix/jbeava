package lib.gintec_rdl.jbeava.validation.utils;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public interface LocaleUtils {
    static boolean isNullOrEmpty(String string) {
        if (string == null) return true;
        return string.trim().isEmpty();
    }

    static <T extends Number> boolean isNumberInRange(T value, T min, T max) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }

    static String ucFirst(String value) {
        if (value.length() >= 2) {
            return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
        } else {
            return value.toUpperCase();
        }
    }

    static String formatLocalDateTime(LocalDateTime localDateTime) {
        Map<String, Object> model;
        StringTemplateEngine engine;

        model = new LinkedHashMap<>();
        engine = new StringTemplateEngine();

        model.put("month", localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));
        model.put("date", localDateTime.getDayOfMonth());
        model.put("year", localDateTime.getYear());
        model.put("hour", localDateTime.getHour());
        model.put("minute", localDateTime.getMinute());
        model.put("second", localDateTime.getSecond());
        return engine.render("${month} ${date} ${year}, ${hour}:${minute}:${second}", model);
    }
}
