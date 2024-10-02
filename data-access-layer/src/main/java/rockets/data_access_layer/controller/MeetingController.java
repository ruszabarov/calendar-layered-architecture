package rockets.data_access_layer.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public ResponseEntity<Meeting> createMeeting(@RequestBody @Valid Meeting meeting) {
        meeting.setTitle(Check.limitString(meeting.getTitle(), 2000));
        meeting.setLocation(Check.limitString(meeting.getLocation(), 2000));
        meeting.setDetails(Check.limitString(meeting.getDetails(), 10000));
        // Attempt to create the meeting
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        if (createdMeeting != null) {
            return ResponseEntity.ok(createdMeeting); // Return 200 with created meeting
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create meeting");
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable UUID id, @RequestBody @Valid Meeting meeting) {
        meeting.setTitle(Check.limitString(meeting.getTitle(), 2000));
        meeting.setLocation(Check.limitString(meeting.getLocation(), 2000));
        meeting.setDetails(Check.limitString(meeting.getDetails(), 10000));
        return meetingService.updateMeeting(id, meeting)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));
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
