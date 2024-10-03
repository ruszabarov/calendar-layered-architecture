package rockets.data_access_layer.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rockets.data_access_layer.dto.CalendarDTO;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.service.CalendarService;
import rockets.data_access_layer.service.MeetingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService calendarService;
    private final MeetingService meetingService;

    public CalendarController(CalendarService calendarService, MeetingService meetingService) {
        this.calendarService = calendarService;
        this.meetingService = meetingService;
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

    @PostMapping(consumes = "application/json")
    public Calendar createCalendar(@RequestBody @Valid CalendarDTO calendarDTO) {
        Calendar calendar = new Calendar();
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setDetails(calendarDTO.getDetails());

        List<Meeting> meetings = meetingService.getAllMeetingsByIds(calendarDTO.getMeetingIds());
        calendar.addMeetings(meetings);

        return calendarService.createCalendar(calendar);
    }

    @PostMapping(value = "/{id}/meetings", consumes = "application/json")
    public ResponseEntity<Calendar> addMeetingsToCalendar(@PathVariable UUID id, @RequestBody List<UUID> meetingIds) {
        return calendarService.addMeetingsToCalendar(id, meetingIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}/meetings", consumes = "application/json")
    public ResponseEntity<Calendar> removeMeetingsFromCalendar(@PathVariable UUID id, @RequestBody List<UUID> meetingIds) {
        return calendarService.removeMeetingsFromCalendar(id, meetingIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Calendar> updateCalendar(@PathVariable UUID id, @RequestBody @Valid CalendarDTO calendarDTO) {
        Calendar calendar = new Calendar();
        calendar.setId(id);
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setDetails(calendarDTO.getDetails());

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
