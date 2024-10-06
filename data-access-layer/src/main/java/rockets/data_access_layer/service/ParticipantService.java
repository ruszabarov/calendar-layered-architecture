package rockets.data_access_layer.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.MeetingRepository;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.*;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    private final MeetingRepository meetingRepository;

    public ParticipantService(ParticipantRepository participantRepository, MeetingRepository meetingRepository) {
        this.participantRepository = participantRepository;
        this.meetingRepository = meetingRepository;
    }

    public List<Participant> getAllParticipants() {
        return this.participantRepository.findAll();
    }

    public List<Participant> getAllParticipantsByIds(Set<UUID> participantIds) {
        return participantRepository.findAllById(participantIds);
    }

    public Optional<Participant> getParticipantById(UUID id) {
        return participantRepository.findById(id);
    }

    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public Optional<Participant> updateParticipant(UUID id, Participant updatedParticipant) {
        return participantRepository.findById(id).map(participant -> {
            participant.setName(updatedParticipant.getName());
            participant.setEmail(updatedParticipant.getEmail());
            return participantRepository.save(participant);
        });
    }

    public void deleteParticipant(UUID id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        Set<Meeting> meetings = new HashSet<>(participant.getMeetings());

        for (Meeting meeting : meetings) {
            meeting.getParticipants().remove(participant);
            participant.getMeetings().remove(meeting);

            if (meeting.getParticipants().isEmpty()) {
                meetingRepository.delete(meeting);
            }
        }

        participantRepository.deleteById(id);
    }
}
