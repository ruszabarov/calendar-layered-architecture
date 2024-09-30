package rockets.data_access_layer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockets.data_access_layer.entity.Meeting;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
}
