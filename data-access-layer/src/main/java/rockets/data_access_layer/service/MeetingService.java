package rockets.data_access_layer.service;

import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.repository.MeetingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
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
}
