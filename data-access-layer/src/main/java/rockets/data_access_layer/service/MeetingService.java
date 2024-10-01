package rockets.data_access_layer.service;

import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.AttachmentRepository;
import rockets.data_access_layer.repository.MeetingRepository;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttachmentRepository attachmentRepository;

    public MeetingService(MeetingRepository meetingRepository, ParticipantRepository participantRepository, AttachmentRepository attachmentRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
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

    public void deleteMeeting(UUID id) {
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
