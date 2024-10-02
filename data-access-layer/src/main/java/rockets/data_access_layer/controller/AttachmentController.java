package rockets.data_access_layer.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.service.AttachmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping
    public List<Attachment> getAllAttachments() {
        return attachmentService.getAllAttachments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable UUID id) {
        return attachmentService.getAttachmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public Attachment createAttachment(@RequestBody Attachment attachment) {
        return attachmentService.createAttachment(attachment);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Attachment> updateAttachment(@PathVariable UUID id, @RequestBody @Valid Attachment attachment) {
        return attachmentService.updateAttachment(id, attachment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable UUID id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
