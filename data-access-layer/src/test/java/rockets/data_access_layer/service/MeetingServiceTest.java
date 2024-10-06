package rockets.data_access_layer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.repository.MeetingRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingService meetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMeetings() {
        Meeting meeting1 = new Meeting();
        UUID randomId1 = UUID.randomUUID();
        meeting1.setId(randomId1);
        meeting1.setTitle("Meeting 1");
        meeting1.setDetails("Details 1");
        meeting1.setDateTime(new Date());
        meeting1.setLocation("Location 1");

        Meeting meeting2 = new Meeting();
        UUID randomId2 = UUID.randomUUID();
        meeting2.setId(randomId2);
        meeting2.setTitle("Meeting 2");
        meeting2.setDetails("Details 2");
        meeting2.setDateTime(new Date());
        meeting2.setLocation("Location 2");

        List<Meeting> meetingList = Arrays.asList(meeting1, meeting2);
        when(meetingRepository.findAll()).thenReturn(meetingList);

        List<Meeting> result = meetingService.getAllMeetings();

        assertEquals(2, result.size());
        assertEquals("Meeting 1", result.get(0).getTitle());
        assertEquals("Meeting 2", result.get(1).getTitle());
        verify(meetingRepository, times(1)).findAll();
    }

    @Test
    void testGetMeetingById() {
        Meeting meeting = new Meeting();
        UUID randomId = UUID.randomUUID();
        meeting.setId(randomId);
        meeting.setTitle("Sample Meeting");
        meeting.setDetails("Sample Details");
        meeting.setDateTime(new Date());
        meeting.setLocation("Sample Location");

        when(meetingRepository.findById(randomId)).thenReturn(Optional.of(meeting));

        Optional<Meeting> result = meetingService.getMeetingById(randomId);

        assertTrue(result.isPresent());
        assertEquals("Sample Meeting", result.get().getTitle());
        verify(meetingRepository, times(1)).findById(randomId);
    }

    @Test
    void testCreateMeeting() {
        Meeting newMeeting = new Meeting();
        UUID randomId = UUID.randomUUID();
        newMeeting.setId(randomId);
        newMeeting.setTitle("New Meeting");
        newMeeting.setDetails("New Details");
        newMeeting.setDateTime(new Date());
        newMeeting.setLocation("New Location");

        when(meetingRepository.save(any(Meeting.class))).thenReturn(newMeeting);

        Meeting createdMeeting = meetingService.createMeeting(newMeeting);

        assertEquals("New Meeting", createdMeeting.getTitle());
        verify(meetingRepository, times(1)).save(newMeeting);
    }

    @Test
    void testUpdateMeeting() {
        // Arrange
        Meeting existingMeeting = new Meeting();
        UUID randomId = UUID.randomUUID();
        existingMeeting.setId(randomId);
        existingMeeting.setTitle("Old Meeting");
        existingMeeting.setDetails("Old Details");
        Date oldDateTime = new Date(0);
        existingMeeting.setDateTime(oldDateTime);
        existingMeeting.setLocation("Old Location");

        Meeting updatedMeeting = new Meeting();
        updatedMeeting.setTitle("Updated Meeting");
        updatedMeeting.setDetails("Updated Details");
        Date updatedDateTime = new Date();
        updatedMeeting.setDateTime(updatedDateTime);
        updatedMeeting.setLocation("Updated Location");

        when(meetingRepository.findById(randomId)).thenReturn(Optional.of(existingMeeting));
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.updateMeeting(randomId, updatedMeeting);

        assertTrue(result.isPresent());
        assertEquals("Updated Meeting", result.get().getTitle());
        assertEquals("Updated Details", result.get().getDetails());
        assertEquals(updatedDateTime, result.get().getDateTime());
        assertEquals("Updated Location", result.get().getLocation());
        verify(meetingRepository, times(1)).findById(randomId);
        verify(meetingRepository, times(1)).save(existingMeeting);
    }

    @Test
    void testDeleteMeeting() {
        UUID randomId = UUID.randomUUID();
        Meeting meeting = new Meeting();
        meeting.setId(randomId);
        meeting.setCalendars(new HashSet<>());

        when(meetingRepository.findById(randomId)).thenReturn(Optional.of(meeting));
        doNothing().when(meetingRepository).deleteById(randomId);

        meetingService.deleteMeeting(randomId);
        verify(meetingRepository, times(1)).deleteById(randomId);
    }
}

