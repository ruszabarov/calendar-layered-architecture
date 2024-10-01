package rockets.data_access_layer.service;

import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.repository.CalendarRepository;
import rockets.data_access_layer.repository.MeetingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final MeetingRepository meetingRepository;

    public CalendarService(CalendarRepository calendarRepository, MeetingRepository meetingRepository) {
        this.calendarRepository = calendarRepository;
        this.meetingRepository = meetingRepository;
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public Optional<Calendar> getCalendarById(UUID id) {
        return calendarRepository.findById(id);
    }

    public Calendar createCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    public Optional<Calendar> updateCalendar(UUID id, Calendar updatedCalendar) {
        return calendarRepository.findById(id).map(calendar -> {
            calendar.setTitle(updatedCalendar.getTitle());
            calendar.setDetails(updatedCalendar.getDetails());

            return calendarRepository.save(calendar);
        });
    }

    public Optional<Calendar> addMeetingsToCalendar(UUID id, List<UUID> calendarIds) {
        return calendarRepository.findById(id).map(calendar -> {
            List<Meeting> meetings = meetingRepository.findAllById(calendarIds);
            calendar.addMeetings(meetings);
            return calendarRepository.save(calendar);
        });
    }

    public Optional<Calendar> removeMeetingsFromCalendar(UUID id, List<UUID> calendarIds) {
        return calendarRepository.findById(id).map(calendar -> {
            List<Meeting> meetings = meetingRepository.findAllById(calendarIds);
            calendar.removeMeetings(meetings);
            return calendarRepository.save(calendar);
        });
    }

    public void deleteCalendar(UUID id) {
        calendarRepository.deleteById(id);
    }
}
