package rockets.data_access_layer.controller;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> createAttachment(@RequestBody Attachment attachment) {
        if (!Check.isValidURL(attachment.getUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }
        Attachment createdAttachment = attachmentService.createAttachment(attachment);
        if (createdAttachment != null) {
            return ResponseEntity.ok((Object) createdAttachment); // Return 200 with created meeting
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to create meeting"); // Return 500 with error message if creation fails
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateAttachment(@PathVariable UUID id, @RequestBody Attachment attachment) {
        if (!Check.isValidURL(attachment.getUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }
        return attachmentService.updateAttachment(id, attachment)
                .map(updatedAttachment -> ResponseEntity.ok((Object) updatedAttachment))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable UUID id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
