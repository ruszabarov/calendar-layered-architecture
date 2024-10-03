package rockets.data_access_layer.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rockets.data_access_layer.dto.MeetingDTO;
import rockets.data_access_layer.entity.Meeting;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.service.MeetingService;
import rockets.data_access_layer.service.ParticipantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;
    private final ParticipantService participantService;

    public MeetingController(MeetingService meetingService, ParticipantService participantService) {
        this.meetingService = meetingService;
        this.participantService = participantService;
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
    public Meeting createMeeting(@RequestBody @Valid MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting();
        meeting.setId(meetingDTO.getId());
        meeting.setTitle(meetingDTO.getTitle());
        meeting.setDetails(meetingDTO.getDetails());
        meeting.setDateTime(meetingDTO.getDateTime());
        meeting.setLocation(meeting.getLocation());

        List<Participant> participants = participantService.getAllParticipantsByIds(meetingDTO.getParticipantIds());
        meeting.addParticipants(participants);

        return meetingService.createMeeting(meeting);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable UUID id, @RequestBody @Valid MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting();
        meeting.setId(meetingDTO.getId());
        meeting.setTitle(meetingDTO.getTitle());
        meeting.setDetails(meetingDTO.getDetails());
        meeting.setDateTime(meetingDTO.getDateTime());

        return meetingService.updateMeeting(id, meeting)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
