package rockets.data_access_layer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.service.CalendarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<Calendar> getAllCalendars() {
        return calendarService.getAllCalendars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarById(@PathVariable UUID id) {
        return calendarService.getCalendarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Calendar createCalendar(@RequestBody Calendar calendar) {
        return calendarService.createCalendar(calendar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calendar> updateCalendar(@PathVariable UUID id, @RequestBody Calendar calendar) {
        return calendarService.updateCalendar(id, calendar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable UUID id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }
}
