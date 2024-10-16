package rockets.data_access_layer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.service.MeetingService;
import rockets.data_access_layer.service.ParticipantService;
import rockets.data_access_layer.util.Utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeetingControllerTest {

    @Mock
    MeetingService meetingService;

    @Mock
    ParticipantService participantService;

    @InjectMocks
    MeetingController meetingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(meetingController).build();
    }

    @Test
    void testGetAllMeetings() throws Exception {
        Meeting meeting1 = new Meeting();
        UUID randomId1 = UUID.randomUUID();
        meeting1.setId(randomId1);
        meeting1.setTitle("Meeting 1");
        meeting1.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        meeting1.setLocation("Location 1");
        meeting1.setDetails("Details 1");

        Meeting meeting2 = new Meeting();
        UUID randomId2 = UUID.randomUUID();
        meeting2.setId(randomId2);
        meeting2.setTitle("Meeting 2");
        meeting2.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        meeting2.setLocation("Location 2");
        meeting2.setDetails("Details 2");

        when(meetingService.getAllMeetings()).thenReturn(Arrays.asList(meeting1, meeting2));

        mockMvc.perform(get("/meetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Meeting 1"))
                .andExpect(jsonPath("$[1].title").value("Meeting 2"));

        verify(meetingService, times(1)).getAllMeetings();
    }

    @Test
    void testGetMeetingById() throws Exception {
        Meeting meeting = new Meeting();
        UUID randomId = UUID.randomUUID();
        meeting.setId(randomId);
        meeting.setTitle("Meeting Title");
        meeting.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        meeting.setLocation("Meeting Location");
        meeting.setDetails("Meeting Details");

        when(meetingService.getMeetingById(randomId)).thenReturn(Optional.of(meeting));

        mockMvc.perform(get("/meetings/{id}", randomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Meeting Title"));

        verify(meetingService, times(1)).getMeetingById(randomId);
    }

    @Test
    void testGetMeetingByIdNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(meetingService.getMeetingById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/meetings/{id}", randomId))
                .andExpect(status().isNotFound());

        verify(meetingService, times(1)).getMeetingById(randomId);
    }

    @Test
    void testCreateMeeting() throws Exception {
        Meeting newMeeting = new Meeting();
        UUID randomId = UUID.randomUUID();
        newMeeting.setId(randomId);
        newMeeting.setTitle("New Meeting");
        newMeeting.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        newMeeting.setLocation("New Location");
        newMeeting.setDetails("New Details");

        Participant participant = new Participant();
        participant.setId(UUID.randomUUID());
        participant.setName("Ruslan");
        participant.setEmail("rxz455@case.edu");

        newMeeting.addParticipants(List.of(participant));

        when(meetingService.createMeeting(any(Meeting.class))).thenReturn(newMeeting);
        when(participantService.getAllParticipantsByIds(anySet())).thenReturn(List.of(participant));

        mockMvc.perform(post("/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(newMeeting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Meeting"));

        verify(meetingService, times(1)).createMeeting(any(Meeting.class));
    }

    @Test
    void testUpdateMeeting() throws Exception {
        UUID randomId = UUID.randomUUID();

        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(randomId);
        existingMeeting.setTitle("Old Title");
        existingMeeting.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        existingMeeting.setLocation("Old Location");
        existingMeeting.setDetails("Old Details");

        Meeting updatedMeeting = new Meeting();
        updatedMeeting.setTitle("Updated Title");
        updatedMeeting.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        updatedMeeting.setLocation("Updated Location");
        updatedMeeting.setDetails("Updated Details");

        when(meetingService.updateMeeting(eq(randomId), any(Meeting.class))).thenReturn(Optional.of(updatedMeeting));

        mockMvc.perform(put("/meetings/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(updatedMeeting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(meetingService, times(1)).updateMeeting(eq(randomId), any(Meeting.class));
    }

    @Test
    void testUpdateMeetingNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        Meeting updatedMeeting = new Meeting();
        updatedMeeting.setTitle("Updated Title");
        updatedMeeting.setDateTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        updatedMeeting.setLocation("Updated Location");
        updatedMeeting.setDetails("Updated Details");

        when(meetingService.updateMeeting(eq(randomId), any(Meeting.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/meetings/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(updatedMeeting)))
                .andExpect(status().isNotFound());

        verify(meetingService, times(1)).updateMeeting(eq(randomId), any(Meeting.class));
    }

    @Test
    void testDeleteMeeting() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete("/meetings/{id}", randomId))
                .andExpect(status().isNoContent());

        verify(meetingService, times(1)).deleteMeeting(randomId);
    }
}
