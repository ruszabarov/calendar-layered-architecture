package rockets.data_access_layer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.service.ParticipantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable UUID id) {
        return participantService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        participant.setName(Check.limitString(participant.getName(),600));
        if (!Check.isValidEmail(participant.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        Participant createdParticipant = participantService.createParticipant(participant);
        if (createdParticipant != null) {
            return ResponseEntity.ok(createdParticipant);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create participant");
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Participant> updateParticipant(@PathVariable UUID id, @RequestBody Participant participant) {
        participant.setName(Check.limitString(participant.getName(),600));
        if (!Check.isValidEmail(participant.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        return participantService.updateParticipant(id, participant)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable UUID id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
}
