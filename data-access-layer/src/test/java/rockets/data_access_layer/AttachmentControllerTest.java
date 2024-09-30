package rockets.data_access_layer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rockets.data_access_layer.controller.AttachmentController;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.service.AttachmentService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttachmentControllerTest {
    @Mock
    AttachmentService attachmentService;

    @InjectMocks
    AttachmentController attachmentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();
    }

    @Test
    void testGetAllAttachments() throws Exception {
        Attachment attachment1 = new Attachment();
        UUID randomId1 = UUID.randomUUID();
        attachment1.setId(randomId1);
        attachment1.setUrl("some-url1");

        Attachment attachment2 = new Attachment();
        UUID randomId2 = UUID.randomUUID();
        attachment2.setId(randomId2);
        attachment2.setUrl("some-url2");

        when(attachmentService.getAllAttachments()).thenReturn(Arrays.asList(attachment1, attachment2));

        mockMvc.perform(get("/attachments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("some-url1"))
                .andExpect(jsonPath("$[1].url").value("some-url2"));

        verify(attachmentService, times(1)).getAllAttachments();
    }

    @Test
    void testGetAttachmentById() throws Exception {
        Attachment attachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        attachment.setId(randomId);
        attachment.setUrl("some-url");

        when(attachmentService.getAttachmentById(randomId)).thenReturn(Optional.of(attachment));

        mockMvc.perform(get("/attachments/{id}", randomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("some-url"));

        verify(attachmentService, times(1)).getAttachmentById(randomId);
    }

    @Test
    void testGetAttachmentByIdNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(attachmentService.getAttachmentById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/attachments/{id}", randomId))
                .andExpect(status().isNotFound());

        verify(attachmentService, times(1)).getAttachmentById(randomId);
    }

    @Test
    void testCreateAttachment() throws Exception {
        Attachment newAttachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        newAttachment.setId(randomId);
        newAttachment.setUrl("some-url");

        when(attachmentService.createAttachment(any(Attachment.class))).thenReturn(newAttachment);

        mockMvc.perform(post("/attachments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newAttachment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("some-url"));

        verify(attachmentService, times(1)).createAttachment(any(Attachment.class));
    }

    @Test
    void testUpdateAttachment() throws Exception {
        Attachment existingAttachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        existingAttachment.setId(randomId);
        existingAttachment.setUrl("some-url");

        Attachment updatedAttachment = new Attachment();
        updatedAttachment.setUrl("new-url");

        when(attachmentService.updateAttachment(eq(randomId), any(Attachment.class))).thenReturn(Optional.of(updatedAttachment));

        mockMvc.perform(put("/attachments/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedAttachment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("new-url"));

        verify(attachmentService, times(1)).updateAttachment(eq(randomId), any(Attachment.class));
    }

    @Test
    void testUpdateAttachmentNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        Attachment wrongAttachment = new Attachment();
        wrongAttachment.setUrl("new-url");

        when(attachmentService.updateAttachment(eq(randomId), any(Attachment.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/attachments/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongAttachment)))
                .andExpect(status().isNotFound());

        verify(attachmentService, times(1)).updateAttachment(eq(randomId), any(Attachment.class));
    }

    @Test
    void testDeleteAttachment() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete("/attachments/{id}", randomId))
                .andExpect(status().isNoContent());

        verify(attachmentService, times(1)).deleteAttachment(randomId);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
