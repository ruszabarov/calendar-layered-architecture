package rockets.data_access_layer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.service.MeetingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable UUID id) {
        return meetingService.getMeetingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createMeeting(@RequestBody Meeting meeting) {
        meeting.setTitle(Check.limitString(meeting.getTitle(), 2000));
        meeting.setLocation(Check.limitString(meeting.getLocation(), 2000));
        meeting.setDetails(Check.limitString(meeting.getDetails(), 10000));
        if (!Check.validateDateTime(meeting.getDateTime().toString())) {
            return ResponseEntity.badRequest().body("Invalid DateTime format"); // Return 400 with error message
        }
        // Attempt to create the meeting
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        if (createdMeeting != null) {
            return ResponseEntity.ok((Object) createdMeeting); // Return 200 with created meeting
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to create meeting"); // Return 500 with error message if creation fails
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateMeeting(@PathVariable UUID id, @RequestBody Meeting meeting) {
        meeting.setTitle(Check.limitString(meeting.getTitle(), 2000));
        meeting.setLocation(Check.limitString(meeting.getLocation(), 2000));
        meeting.setDetails(Check.limitString(meeting.getDetails(), 10000));
        if (!Check.validateDateTime(meeting.getDateTime().toString())) {
            return ResponseEntity.badRequest().body("Invalid DateTime format"); // Return 400 with error message
        }
        return meetingService.updateMeeting(id, meeting)
                .map(updatedMeeting -> ResponseEntity.ok((Object) updatedMeeting)) // Cast updatedMeeting to Object
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    @PostMapping(value = "/{id}/participants", consumes = "application/json")
    public ResponseEntity<Meeting> addParticipantsToMeeting(@PathVariable UUID id, @RequestBody List<UUID> participantIds) {
        return meetingService.addParticipantsToMeeting(id, participantIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}/participants", consumes = "application/json")
    public ResponseEntity<Meeting> removeParticipantsFromMeeting(@PathVariable UUID id, @RequestBody List<UUID> participantIds) {
        return meetingService.removeParticipantsFromMeeting(id, participantIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/attachments", consumes = "application/json")
    public ResponseEntity<Meeting> addAttachmentsToMeeting(@PathVariable UUID id, @RequestBody List<UUID> attachmentIds) {
        return meetingService.addAttachmentToMeeting(id, attachmentIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}/attachments", consumes = "application/json")
    public ResponseEntity<Meeting> removeAttachmentstoMeeting(@PathVariable UUID id, @RequestBody List<UUID> attachmentIds) {
        return meetingService.removeAttachmentsFromMeeting(id, attachmentIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable UUID id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.noContent().build();
    }
}
