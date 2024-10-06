package rockets.data_access_layer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.repository.CalendarRepository;
import rockets.data_access_layer.repository.MeetingRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CalendarServiceTest {

    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private CalendarService calendarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCalendars() {
        Calendar calendar1 = new Calendar();
        UUID randomId1 = UUID.randomUUID();
        calendar1.setId(randomId1);
        calendar1.setTitle("Calendar 1");
        calendar1.setDetails("Details 1");

        Calendar calendar2 = new Calendar();
        UUID randomId2 = UUID.randomUUID();
        calendar2.setId(randomId2);
        calendar2.setTitle("Calendar 2");
        calendar2.setDetails("Details 2");

        List<Calendar> calendarList = Arrays.asList(calendar1, calendar2);
        when(calendarRepository.findAll()).thenReturn(calendarList);

        List<Calendar> result = calendarService.getAllCalendars();

        assertEquals(2, result.size());
        assertEquals("Calendar 1", result.get(0).getTitle());
        assertEquals("Calendar 2", result.get(1).getTitle());
        verify(calendarRepository, times(1)).findAll();
    }

    @Test
    void testGetCalendarById() {
        Calendar calendar = new Calendar();
        UUID randomId = UUID.randomUUID();
        calendar.setId(randomId);
        calendar.setTitle("Sample Calendar");
        calendar.setDetails("Sample Details");

        when(calendarRepository.findById(randomId)).thenReturn(Optional.of(calendar));

        Optional<Calendar> result = calendarService.getCalendarById(randomId);

        assertTrue(result.isPresent());
        assertEquals("Sample Calendar", result.get().getTitle());
        verify(calendarRepository, times(1)).findById(randomId);
    }

    @Test
    void testCreateCalendar() {
        Calendar newCalendar = new Calendar();
        UUID randomId = UUID.randomUUID();
        newCalendar.setId(randomId);
        newCalendar.setTitle("New Calendar");
        newCalendar.setDetails("New Details");

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID());
        newCalendar.addMeetings(List.of(meeting));

        when(calendarRepository.save(any(Calendar.class))).thenReturn(newCalendar);

        Calendar createdCalendar = calendarService.createCalendar(newCalendar);

        assertEquals("New Calendar", createdCalendar.getTitle());
        assertEquals("New Details", createdCalendar.getDetails());
        assertTrue(createdCalendar.getMeetings().contains(meeting));
        verify(calendarRepository, times(1)).save(newCalendar);
    }

    @Test
    void testUpdateCalendar() {
        Calendar existingCalendar = new Calendar();
        UUID randomId = UUID.randomUUID();
        existingCalendar.setId(randomId);
        existingCalendar.setTitle("Old Calendar");
        existingCalendar.setDetails("Old Details");

        Calendar updatedCalendar = new Calendar();
        updatedCalendar.setTitle("Updated Calendar");
        updatedCalendar.setDetails("Updated Details");

        when(calendarRepository.findById(randomId)).thenReturn(Optional.of(existingCalendar));
        when(calendarRepository.save(existingCalendar)).thenReturn(existingCalendar);

        Optional<Calendar> result = calendarService.updateCalendar(randomId, updatedCalendar);

        assertTrue(result.isPresent());
        assertEquals("Updated Calendar", result.get().getTitle());
        assertEquals("Updated Details", result.get().getDetails());
        verify(calendarRepository, times(1)).findById(randomId);
        verify(calendarRepository, times(1)).save(existingCalendar);
    }

    @Test
    void testDeleteCalendar() {
        UUID randomId = UUID.randomUUID();

        calendarService.deleteCalendar(randomId);

        verify(calendarRepository, times(1)).deleteById(randomId);
    }
}
