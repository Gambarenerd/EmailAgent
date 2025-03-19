package org.example.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final Calendar calendarClient;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'alle' HH:mm");


    public CalendarService(Calendar calendarClient) {
        this.calendarClient = calendarClient;
    }

    public String getEventsForDate(LocalDate date) throws IOException {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault());

        DateTime startDateTime = new DateTime(startOfDay.toInstant().toEpochMilli());
        DateTime endDateTime = new DateTime(endOfDay.toInstant().toEpochMilli());

        Events events = calendarClient.events().list("primary")
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems().stream()
                .map(this::formatEventAsString)
                .collect(Collectors.joining("\n"));
    }

    private String formatEventAsString(Event event) {
        ZonedDateTime start = ZonedDateTime.parse(event.getStart().getDateTime().toStringRfc3339());
        ZonedDateTime end = ZonedDateTime.parse(event.getEnd().getDateTime().toStringRfc3339());

        return String.format("Event: %s, start on %s, end on %s",
                event.getSummary(),
                formatter.format(start),
                formatter.format(end));
    }

    public String createEvent(String summary, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) throws IOException {

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTime start = new DateTime(startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        DateTime end = new DateTime(endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        event.setStart(new EventDateTime().setDateTime(start));
        event.setEnd(new EventDateTime().setDateTime(end));

        calendarClient.events().insert("primary", event).execute();

        return String.format("Event created: %s\nDescription: %s\nDate: %s\nStarting at: %s\nEnding at: %s",
                summary,
                description,
                startDateTime.toLocalDate(),
                startDateTime.toLocalTime(),
                endDateTime.toLocalTime());
    }
}