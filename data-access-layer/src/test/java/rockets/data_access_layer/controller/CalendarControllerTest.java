package rockets.data_access_layer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rockets.data_access_layer.dto.CalendarDTO;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.service.CalendarService;
import rockets.data_access_layer.service.MeetingService;
import rockets.data_access_layer.util.Utility;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CalendarControllerTest {
    @Mock
    CalendarService calendarService;

    @Mock
    MeetingService meetingService;

    @InjectMocks
    CalendarController calendarController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
    }

    @Test
    void testGetAllCalendars() throws Exception {
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

        when(calendarService.getAllCalendars()).thenReturn(Arrays.asList(calendar1, calendar2));

        mockMvc.perform(get("/calendars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Calendar 1"))
                .andExpect(jsonPath("$[1].title").value("Calendar 2"));

        verify(calendarService, times(1)).getAllCalendars();
    }

    @Test
    void testGetCalendarById() throws Exception {
        Calendar calendar = new Calendar();
        UUID randomId = UUID.randomUUID();
        calendar.setId(randomId);
        calendar.setTitle("Test Calendar");
        calendar.setDetails("Test Details");

        when(calendarService.getCalendarById(randomId)).thenReturn(Optional.of(calendar));

        mockMvc.perform(get("/calendars/{id}", randomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Calendar"));

        verify(calendarService, times(1)).getCalendarById(randomId);
    }

    @Test
    void testGetCalendarByIdNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(calendarService.getCalendarById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/calendars/{id}", randomId))
                .andExpect(status().isNotFound());

        verify(calendarService, times(1)).getCalendarById(randomId);
    }

    @Test
    void testCreateCalendar() throws Exception {
        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setId(UUID.randomUUID());
        calendarDTO.setTitle("New Calendar");
        calendarDTO.setDetails("New Details");

        Calendar createdCalendar = new Calendar();
        createdCalendar.setId(calendarDTO.getId());
        createdCalendar.setTitle(calendarDTO.getTitle());
        createdCalendar.setDetails(calendarDTO.getDetails());

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID());
        meeting.setTitle("some meeting title");

        createdCalendar.addMeetings(List.of(meeting));

        when(calendarService.createCalendar(any(Calendar.class))).thenReturn(createdCalendar);
        when(meetingService.getAllMeetingsByIds(anySet())).thenReturn(List.of(meeting));

        mockMvc.perform(post("/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(calendarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Calendar"))
                .andExpect(jsonPath("$.id").value(calendarDTO.getId().toString()));

        verify(calendarService, times(1)).createCalendar(any(Calendar.class));
    }

    @Test
    void testUpdateCalendar() throws Exception {
        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setId(UUID.randomUUID());
        calendarDTO.setTitle("Updated Title");
        calendarDTO.setDetails("Updated Details");

        Calendar updatedCalendar = new Calendar();
        updatedCalendar.setId(calendarDTO.getId());
        updatedCalendar.setTitle(calendarDTO.getTitle());
        updatedCalendar.setDetails(calendarDTO.getDetails());
        updatedCalendar.setMeetings(new HashSet<>(List.of(new Meeting())));

        when(calendarService.updateCalendar(any(UUID.class), any(Calendar.class))).thenReturn(Optional.of(updatedCalendar));

        mockMvc.perform(put("/calendars/{id}", calendarDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(calendarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.details").value("Updated Details"));

        verify(calendarService, times(1)).updateCalendar(eq(calendarDTO.getId()), any(Calendar.class));
    }

    @Test
    void testUpdateCalendarNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        Calendar updatedCalendar = new Calendar();
        updatedCalendar.setTitle("Updated Title");
        updatedCalendar.setDetails("Updated Details");

        when(calendarService.updateCalendar(eq(randomId), any(Calendar.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/calendars/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(updatedCalendar)))
                .andExpect(status().isNotFound());

        verify(calendarService, times(1)).updateCalendar(eq(randomId), any(Calendar.class));
    }

    @Test
    void testDeleteCalendar() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete("/calendars/{id}", randomId))
                .andExpect(status().isNoContent());

        verify(calendarService, times(1)).deleteCalendar(randomId);
    }
}
