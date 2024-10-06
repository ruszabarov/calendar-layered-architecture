package rockets.data_access_layer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.repository.ParticipantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllParticipants() {
        Participant participant1 = new Participant();
        UUID randomId1 = UUID.randomUUID();
        participant1.setId(randomId1);
        participant1.setName("Participant One");
        participant1.setEmail("one@example.com");

        Participant participant2 = new Participant();
        UUID randomId2 = UUID.randomUUID();
        participant2.setId(randomId2);
        participant2.setName("Participant Two");
        participant2.setEmail("two@example.com");

        List<Participant> participantList = Arrays.asList(participant1, participant2);
        when(participantRepository.findAll()).thenReturn(participantList);

        List<Participant> result = participantService.getAllParticipants();

        assertEquals(2, result.size());
        assertEquals("Participant One", result.get(0).getName());
        assertEquals("Participant Two", result.get(1).getName());
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void testGetParticipantById() {
        Participant participant = new Participant();
        UUID randomId = UUID.randomUUID();
        participant.setId(randomId);
        participant.setName("Participant Name");
        participant.setEmail("participant@example.com");

        when(participantRepository.findById(randomId)).thenReturn(Optional.of(participant));

        Optional<Participant> result = participantService.getParticipantById(randomId);

        assertTrue(result.isPresent());
        assertEquals("Participant Name", result.get().getName());
        assertEquals("participant@example.com", result.get().getEmail());
        verify(participantRepository, times(1)).findById(randomId);
    }

    @Test
    void testCreateParticipant() {
        Participant newParticipant = new Participant();
        newParticipant.setName("New Participant");
        newParticipant.setEmail("new@example.com");

        Participant savedParticipant = new Participant();
        UUID randomId = UUID.randomUUID();
        savedParticipant.setId(randomId);
        savedParticipant.setName("New Participant");
        savedParticipant.setEmail("new@example.com");

        when(participantRepository.save(any(Participant.class))).thenReturn(savedParticipant);

        Participant createdParticipant = participantService.createParticipant(newParticipant);

        assertEquals("New Participant", createdParticipant.getName());
        assertEquals("new@example.com", createdParticipant.getEmail());
        verify(participantRepository, times(1)).save(newParticipant);
    }

    @Test
    void testUpdateParticipant() {
        UUID randomId = UUID.randomUUID();

        Participant existingParticipant = new Participant();
        existingParticipant.setId(randomId);
        existingParticipant.setName("Existing Participant");
        existingParticipant.setEmail("existing@example.com");

        Participant updatedParticipant = new Participant();
        updatedParticipant.setName("Updated Participant");
        updatedParticipant.setEmail("updated@example.com");

        when(participantRepository.findById(randomId)).thenReturn(Optional.of(existingParticipant));
        when(participantRepository.save(any(Participant.class))).thenReturn(existingParticipant);

        Optional<Participant> result = participantService.updateParticipant(randomId, updatedParticipant);

        assertTrue(result.isPresent());
        assertEquals("Updated Participant", result.get().getName());
        assertEquals("updated@example.com", result.get().getEmail());
        verify(participantRepository, times(1)).findById(randomId);
        verify(participantRepository, times(1)).save(existingParticipant);
    }

    @Test
    void testDeleteParticipant() {
        UUID randomId = UUID.randomUUID();
        Participant participant = new Participant();

        when(participantRepository.findById(randomId)).thenReturn(Optional.of(participant));

        doNothing().when(participantRepository).deleteById(randomId);

        participantService.deleteParticipant(randomId);

        verify(participantRepository, times(1)).deleteById(randomId);
    }
}
