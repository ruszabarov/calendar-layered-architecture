package rockets.data_access_layer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rockets.data_access_layer.entity.Participant;
import rockets.data_access_layer.service.ParticipantService;
import rockets.data_access_layer.util.Utility;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantControllerTest {

    @Mock
    ParticipantService participantService;

    @InjectMocks
    ParticipantController participantController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    @Test
    void testGetAllParticipants() throws Exception {
        Participant participant1 = new Participant();
        UUID randomId1 = UUID.randomUUID();
        participant1.setId(randomId1);
        participant1.setName("John Doe");
        participant1.setEmail("johndoe@email.com");

        Participant participant2 = new Participant();
        UUID randomId2 = UUID.randomUUID();
        participant2.setId(randomId2);
        participant2.setName("Jane Smith");
        participant2.setEmail("janesmith@email.com");

        when(participantService.getAllParticipants()).thenReturn(Arrays.asList(participant1, participant2));

        mockMvc.perform(get("/participants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@email.com"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].email").value("janesmith@email.com"));

        verify(participantService, times(1)).getAllParticipants();
    }

    @Test
    void testGetParticipantById() throws Exception {
        Participant participant = new Participant();
        UUID randomId = UUID.randomUUID();
        participant.setId(randomId);
        participant.setName("John Doe");
        participant.setEmail("johndoe@email.com");

        when(participantService.getParticipantById(randomId)).thenReturn(Optional.of(participant));

        mockMvc.perform(get("/participants/{id}", randomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@email.com"));

        verify(participantService, times(1)).getParticipantById(randomId);
    }

    @Test
    void testGetParticipantByIdNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(participantService.getParticipantById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/participants/{id}", randomId))
                .andExpect(status().isNotFound());

        verify(participantService, times(1)).getParticipantById(randomId);
    }

    @Test
    void testCreateParticipant() throws Exception {
        Participant newParticipant = new Participant();
        UUID randomId = UUID.randomUUID();
        newParticipant.setId(randomId);
        newParticipant.setName("John Doe");
        newParticipant.setEmail("johndoe@email.com");

        when(participantService.createParticipant(any(Participant.class))).thenReturn(newParticipant);

        mockMvc.perform(post("/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(newParticipant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@email.com"));

        verify(participantService, times(1)).createParticipant(any(Participant.class));
    }

    @Test
    void testUpdateParticipant() throws Exception {
        Participant existingParticipant = new Participant();
        UUID randomId = UUID.randomUUID();
        existingParticipant.setId(randomId);
        existingParticipant.setName("John Doe");
        existingParticipant.setEmail("johndoe@email.com");

        Participant updatedParticipant = new Participant();
        updatedParticipant.setName("Jane Smith");
        updatedParticipant.setEmail("janesmith@email.com");

        when(participantService.updateParticipant(eq(randomId), any(Participant.class))).thenReturn(Optional.of(updatedParticipant));

        mockMvc.perform(put("/participants/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(updatedParticipant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("janesmith@email.com"));

        verify(participantService, times(1)).updateParticipant(eq(randomId), any(Participant.class));
    }

    @Test
    void testUpdateParticipantNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        Participant participantToUpdate = new Participant();
        participantToUpdate.setName("Jane Smith");
        participantToUpdate.setEmail("janesmith@email.com");

        when(participantService.updateParticipant(eq(randomId), any(Participant.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/participants/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utility.asJsonString(participantToUpdate)))
                .andExpect(status().isNotFound());

        verify(participantService, times(1)).updateParticipant(eq(randomId), any(Participant.class));
    }

    @Test
    void testDeleteParticipant() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete("/participants/{id}", randomId))
                .andExpect(status().isNoContent());

        verify(participantService, times(1)).deleteParticipant(randomId);
    }
}
