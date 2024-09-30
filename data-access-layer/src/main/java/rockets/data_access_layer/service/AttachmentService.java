package rockets.data_access_layer.service;

import org.springframework.stereotype.Service;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.repository.AttachmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public Optional<Attachment> getAttachmentById(UUID id) {
        return attachmentRepository.findById(id);
    }

    public Attachment createAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    public Optional<Attachment> updateAttachment(UUID id, Attachment updatedAttachment) {
        return attachmentRepository.findById(id).map(attachment -> {
            attachment.setUrl(updatedAttachment.getUrl());
            return attachmentRepository.save(attachment);
        });
    }

    public void deleteAttachment(UUID id) {
        attachmentRepository.deleteById(id);
    }
}
