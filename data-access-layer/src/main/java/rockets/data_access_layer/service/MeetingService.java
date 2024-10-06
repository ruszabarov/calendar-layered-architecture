package rockets.data_access_layer.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.entity.Calendar;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.AttachmentRepository;
import rockets.data_access_layer.repository.CalendarRepository;
import rockets.data_access_layer.repository.MeetingRepository;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.*;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttachmentRepository attachmentRepository;

    private final CalendarRepository calendarRepository;

    public MeetingService(MeetingRepository meetingRepository, ParticipantRepository participantRepository, AttachmentRepository attachmentRepository, CalendarRepository calendarRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attachmentRepository = attachmentRepository;
        this.calendarRepository = calendarRepository;
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public List<Meeting> getAllMeetingsByIds(Set<UUID> meetingIds) {
        return meetingRepository.findAllById(meetingIds);
    }

    public Optional<Meeting> getMeetingById(UUID id) {
        return meetingRepository.findById(id);
    }

    public Meeting createMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Optional<Meeting> updateMeeting(UUID id, Meeting updatedMeeting) {
        return meetingRepository.findById(id).map(meeting -> {
            meeting.setTitle(updatedMeeting.getTitle());
            meeting.setDetails(updatedMeeting.getDetails());
            meeting.setDateTime(updatedMeeting.getDateTime());
            meeting.setLocation(updatedMeeting.getLocation());

            return meetingRepository.save(meeting);
        });
    }

    @Transactional
    public void deleteMeeting(UUID id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found"));

        Set<Calendar> calendars = new HashSet<>(meeting.getCalendars());

        for (Calendar calendar : calendars) {
            calendar.getMeetings().remove(meeting);
            meeting.getCalendars().remove(calendar);

            // Step 2: Delete Calendar if it has no more Meetings
            if (calendar.getMeetings().isEmpty()) {
                calendarRepository.delete(calendar);
            }
        }

        meetingRepository.deleteById(id);
    }

    public Optional<Meeting> addParticipantsToMeeting(UUID id, List<UUID> participantIds) {
        return meetingRepository.findById(id).map(meeting -> {
            List<Participant> participants = participantRepository.findAllById(participantIds);
            meeting.addParticipants(participants);
            return meetingRepository.save(meeting);
        });
    }

    public Optional<Meeting> removeParticipantsFromMeeting(UUID id, List<UUID> participantIds) {
        return meetingRepository.findById(id).map(meeting -> {
            List<Participant> participants = participantRepository.findAllById(participantIds);
            meeting.removeParticipants(participants);
            return meetingRepository.save(meeting);
        });
    }

    public Optional<Meeting> addAttachmentToMeeting(UUID id, List<UUID> attachmentIds) {
        return meetingRepository.findById(id).map(meeting -> {
            List<Attachment> attachments = attachmentRepository.findAllById(attachmentIds);
            meeting.addAttachments(attachments);
            return meetingRepository.save(meeting);
        });
    }

    public Optional<Meeting> removeAttachmentsFromMeeting(UUID id, List<UUID> attachmentIds) {
        return meetingRepository.findById(id).map(meeting -> {
            List<Attachment> attachments = attachmentRepository.findAllById(attachmentIds);
            meeting.removeAttachments(attachments);
            return meetingRepository.save(meeting);
        });
    }
}
