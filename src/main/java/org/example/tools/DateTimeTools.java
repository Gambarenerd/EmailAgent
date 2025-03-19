package org.example.tools;

import org.springframework.stereotype.Component;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

@Component
public class DateTimeTools {

    @Tool(name = "get_current_date", description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        System.out.println("Data controllata");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
