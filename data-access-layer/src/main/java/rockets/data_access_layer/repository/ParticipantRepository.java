package rockets.data_access_layer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockets.data_access_layer.entity.Participant;

import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
}
