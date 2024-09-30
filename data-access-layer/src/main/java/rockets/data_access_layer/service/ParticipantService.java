package rockets.data_access_layer.service;

import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> getAllParticipants() {
        return this.participantRepository.findAll();
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
        participantRepository.deleteById(id);
    }
}
