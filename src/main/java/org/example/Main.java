package org.example;

import org.example.service.CalendarService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final CalendarService calendarService;

    public Main(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    //This is used for test, change this date to check calendar event
    @Override
    public void run(String... args) throws Exception {
        LocalDate data = LocalDate.of(2025, 3, 22);
        String event = calendarService.getEventsForDate(data);

        System.out.println("[Main Calendar] " + event);

    }
}
