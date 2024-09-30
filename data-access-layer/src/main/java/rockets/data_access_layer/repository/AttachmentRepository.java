package rockets.data_access_layer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockets.data_access_layer.entity.Attachment;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
