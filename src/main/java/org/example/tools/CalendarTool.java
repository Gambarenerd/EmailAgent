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

    @Tool(name = "check_events_by_date", description = "Controlla gli eventi del calendario per una data specifica")
    public String checkCalendarEvents(@ToolParam String year, @ToolParam String month, @ToolParam String day) throws IOException {
        System.out.println("[CalendarTool:checkCalendarEvents] Date1: " + year + "-" + month + "-" + day);

        int effectiveYear;
        if (year == null || year.isEmpty() || year.equalsIgnoreCase("null")) {
            effectiveYear = LocalDate.now().getYear();
            System.out.println("[CalendarTool:checkCalendarEvents:if] effectiveYear: " + effectiveYear);
        } else {
            effectiveYear = Integer.parseInt(year);
            System.out.println("[CalendarTool:checkCalendarEvents:else] effectiveYear: " + effectiveYear);
        }

        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);

        System.out.println("[CalendarTool:checkCalendarEvents] Date2: " + effectiveYear + "-" + month + "-" + day);

        LocalDate data = LocalDate.of(effectiveYear, monthInt, dayInt);
        return calendarService.getEventsForDate(data);
    }

    @Tool(name = "create_calendar_event", description = "Crea un evento nel calendario di Google con il titolo, descrizione, data e orario forniti.")
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
            System.out.println("[CalendarTool:createCalendarEvent] Year: " + year);

            LocalDateTime startDateTime = LocalDateTime.of(year, month, day, hourStart, minuteStart);
            LocalDateTime endDateTime = LocalDateTime.of(year, month, day, hourEnd, minuteEnd);
            return calendarService.createEvent(summary, description, startDateTime, endDateTime);
        } catch (IOException e) {
            return "Errore durante la creazione dell'evento: " + e.getMessage();
        }
    }
}
