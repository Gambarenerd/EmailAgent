package org.example.tools;

import org.example.service.CalendarService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CalendarTool {

    private final CalendarService calendarService;

    public CalendarTool(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Tool(name = "check_events_by_date", description = "Check calendar event for a specific date")
    public String checkCalendarEvents(@ToolParam String year, @ToolParam String month, @ToolParam String day) throws IOException {

        int effectiveYear;
        if (year == null || year.isEmpty() || year.equalsIgnoreCase("null")) {
            effectiveYear = LocalDate.now().getYear();
        } else {
            effectiveYear = Integer.parseInt(year);
        }

        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);

        LocalDate data = LocalDate.of(effectiveYear, monthInt, dayInt);
        return calendarService.getEventsForDate(data);
    }

    @Tool(name = "create_calendar_event", description = "Create an event in Google Calendar with the provided title, description, date, and time.")
    public String createCalendarEvent(
            @ToolParam String summary,
            @ToolParam String description,
            @ToolParam int year,
            @ToolParam int month,
            @ToolParam int day,
            @ToolParam int hourStart,
            @ToolParam int minuteStart,
            @ToolParam int hourEnd,
            @ToolParam int minuteEnd) {
        try {

            LocalDateTime startDateTime = LocalDateTime.of(year, month, day, hourStart, minuteStart);
            LocalDateTime endDateTime = LocalDateTime.of(year, month, day, hourEnd, minuteEnd);
            return calendarService.createEvent(summary, description, startDateTime, endDateTime);
        } catch (IOException e) {
            return "Error during calendar event creation: " + e.getMessage();
        }
    }
}
