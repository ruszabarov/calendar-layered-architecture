package rockets.data_access_layer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.AttachmentRepository;
import rockets.data_access_layer.repository.MeetingRepository;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MeetingServiceTest {
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    ParticipantRepository participantRepository;
    @Mock
    AttachmentRepository attachmentRepository;

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
        meeting.setId(UUID.randomUUID());
        meeting.setTitle("Sample Meeting");
        meeting.setDetails("Sample Details");
        meeting.setDateTime(new Date());
        meeting.setLocation("Sample Location");

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        Optional<Meeting> result = meetingService.getMeetingById(meeting.getId());

        assertTrue(result.isPresent());
        assertEquals("Sample Meeting", result.get().getTitle());
        verify(meetingRepository, times(1)).findById(meeting.getId());
    }

    @Test
    void testGetAllMeetingsByIds() {
        Meeting meeting1 = new Meeting();
        meeting1.setId(UUID.randomUUID());
        meeting1.setTitle("Sample Meeting 1");
        meeting1.setDetails("Sample Details 1");
        meeting1.setDateTime(new Date());
        meeting1.setLocation("Sample Location 1");

        Meeting meeting2 = new Meeting();
        meeting2.setId(UUID.randomUUID());
        meeting2.setTitle("Sample Meeting 2");
        meeting2.setDetails("Sample Details 2");
        meeting2.setDateTime(new Date());
        meeting2.setLocation("Sample Location 2");

        when(meetingRepository.findAllById(anySet())).thenReturn(List.of(meeting1, meeting2));

        List<Meeting> result = meetingService.getAllMeetingsByIds(Set.of(meeting1.getId(), meeting2.getId()));

        assertEquals(2, result.size());
        assertTrue(result.contains(meeting1));
        assertTrue(result.contains(meeting2));
        verify(meetingRepository, times(1)).findAllById(Set.of(meeting1.getId(), meeting2.getId()));
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
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(UUID.randomUUID());
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

        when(meetingRepository.findById(existingMeeting.getId())).thenReturn(Optional.of(existingMeeting));
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.updateMeeting(existingMeeting.getId(), updatedMeeting);

        assertTrue(result.isPresent());
        assertEquals("Updated Meeting", result.get().getTitle());
        assertEquals("Updated Details", result.get().getDetails());
        assertEquals(updatedDateTime, result.get().getDateTime());
        assertEquals("Updated Location", result.get().getLocation());
        verify(meetingRepository, times(1)).findById(existingMeeting.getId());
        verify(meetingRepository, times(1)).save(existingMeeting);
    }

    @Test
    void testDeleteMeeting() {
        UUID randomId = UUID.randomUUID();
        meetingService.deleteMeeting(randomId);
        verify(meetingRepository, times(1)).deleteById(randomId);
    }

    @Test
    void testAddParticipantsToMeeting() {
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(UUID.randomUUID());
        existingMeeting.setTitle("Old Meeting");
        existingMeeting.setDetails("Old Details");
        Date oldDateTime = new Date(0);
        existingMeeting.setDateTime(oldDateTime);
        existingMeeting.setLocation("Old Location");

        Participant participant1 = new Participant();
        participant1.setId(UUID.randomUUID());
        existingMeeting.addParticipants(List.of(participant1));

        Participant participant2 = new Participant();
        participant2.setId(UUID.randomUUID());

        List<Participant> participantsToAdd = List.of(participant2);

        when(meetingRepository.findById(existingMeeting.getId())).thenReturn(Optional.of(existingMeeting));
        when(participantRepository.findAllById(anyList())).thenReturn(participantsToAdd);
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.addParticipantsToMeeting(existingMeeting.getId(), List.of(participant2.getId()));

        assertTrue(result.isPresent());
        assertTrue(result.get().getParticipants().contains(participant2));
        verify(meetingRepository, times(1)).findById(existingMeeting.getId());
        verify(meetingRepository, times(1)).save(existingMeeting);
        verify(participantRepository, times(1)).findAllById(List.of(participant2.getId()));
    }

    @Test
    void testRemoveParticipantsFromMeeting() {
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(UUID.randomUUID());
        existingMeeting.setTitle("Old Meeting");
        existingMeeting.setDetails("Old Details");
        Date oldDateTime = new Date(0);
        existingMeeting.setDateTime(oldDateTime);
        existingMeeting.setLocation("Old Location");

        Participant participant1 = new Participant();
        participant1.setId(UUID.randomUUID());
        Participant participant2 = new Participant();
        participant2.setId(UUID.randomUUID());

        existingMeeting.addParticipants(List.of(participant1, participant2));

        List<Participant> participantsToRemove = List.of(participant2);

        when(meetingRepository.findById(existingMeeting.getId())).thenReturn(Optional.of(existingMeeting));
        when(participantRepository.findAllById(anyList())).thenReturn(participantsToRemove);
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.removeParticipantsFromMeeting(existingMeeting.getId(), List.of(participant2.getId()));

        assertTrue(result.isPresent());
        assertFalse(result.get().getParticipants().contains(participant2));
        verify(meetingRepository, times(1)).findById(existingMeeting.getId());
        verify(meetingRepository, times(1)).save(existingMeeting);
        verify(participantRepository, times(1)).findAllById(List.of(participant2.getId()));
    }

    @Test
    void testAddAttachmentsToMeeting() {
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(UUID.randomUUID());
        existingMeeting.setTitle("Old Meeting");
        existingMeeting.setDetails("Old Details");
        Date oldDateTime = new Date(0);
        existingMeeting.setDateTime(oldDateTime);
        existingMeeting.setLocation("Old Location");

        Attachment attachment1 = new Attachment();
        attachment1.setId(UUID.randomUUID());
        existingMeeting.addAttachments(List.of(attachment1));

        Attachment attachment2 = new Attachment();
        attachment2.setId(UUID.randomUUID());

        List<Attachment> attachmentsToAdd = List.of(attachment2);

        when(meetingRepository.findById(existingMeeting.getId())).thenReturn(Optional.of(existingMeeting));
        when(attachmentRepository.findAllById(anyList())).thenReturn(attachmentsToAdd);
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.addAttachmentToMeeting(existingMeeting.getId(), List.of(attachment2.getId()));

        assertTrue(result.isPresent());
        assertTrue(result.get().getAttachments().contains(attachment2));
        verify(meetingRepository, times(1)).findById(existingMeeting.getId());
        verify(meetingRepository, times(1)).save(existingMeeting);
        verify(attachmentRepository, times(1)).findAllById(List.of(attachment2.getId()));
    }

    @Test
    void testRemoveAttachmentsFromMeeting() {
        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(UUID.randomUUID());
        existingMeeting.setTitle("Old Meeting");
        existingMeeting.setDetails("Old Details");
        Date oldDateTime = new Date(0);
        existingMeeting.setDateTime(oldDateTime);
        existingMeeting.setLocation("Old Location");

        Attachment attachment1 = new Attachment();
        attachment1.setId(UUID.randomUUID());
        Attachment attachment2 = new Attachment();
        attachment2.setId(UUID.randomUUID());
        existingMeeting.addAttachments(List.of(attachment1, attachment2));


        List<Attachment> attachmentsToRemove = List.of(attachment2);

        when(meetingRepository.findById(existingMeeting.getId())).thenReturn(Optional.of(existingMeeting));
        when(attachmentRepository.findAllById(anyList())).thenReturn(attachmentsToRemove);
        when(meetingRepository.save(existingMeeting)).thenReturn(existingMeeting);

        Optional<Meeting> result = meetingService.removeAttachmentsFromMeeting(existingMeeting.getId(), List.of(attachment2.getId()));

        assertTrue(result.isPresent());
        assertFalse(result.get().getAttachments().contains(attachment2));
        verify(meetingRepository, times(1)).findById(existingMeeting.getId());
        verify(meetingRepository, times(1)).save(existingMeeting);
        verify(attachmentRepository, times(1)).findAllById(List.of(attachment2.getId()));
    }
}

