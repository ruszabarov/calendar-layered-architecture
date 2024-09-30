package rockets.data_access_layer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rockets.data_access_layer.entity.Attachment;
import rockets.data_access_layer.repository.AttachmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AttachmentServiceTest {
    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentService attachmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAttachments() {
        Attachment attachment1 = new Attachment();
        UUID randomId1 = UUID.randomUUID();
        attachment1.setId(randomId1);
        attachment1.setUrl("some-url1");

        Attachment attachment2 = new Attachment();
        UUID randomId2 = UUID.randomUUID();
        attachment2.setId(randomId2);
        attachment2.setUrl("some-url2");

        List<Attachment> attachmentList = Arrays.asList(attachment1, attachment2);
        when(attachmentRepository.findAll()).thenReturn(attachmentList);

        List<Attachment> result = attachmentService.getAllAttachments();

        assertEquals(2, result.size());
        assertEquals("some-url1", result.get(0).getUrl());
        assertEquals("some-url2", result.get(1).getUrl());
        verify(attachmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAttachmentById() {
        Attachment attachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        attachment.setId(randomId);
        attachment.setUrl("some-url");

        when(attachmentRepository.findById(randomId)).thenReturn(Optional.of(attachment));

        Optional<Attachment> result = attachmentService.getAttachmentById(randomId);

        assertTrue(result.isPresent());
        assertEquals("some-url", result.get().getUrl());
        verify(attachmentRepository, times(1)).findById(randomId);
    }

    @Test
    void testCreateAttachment() {
        Attachment newAttachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        newAttachment.setId(randomId);
        newAttachment.setUrl("some-url");

        when(attachmentRepository.save(any(Attachment.class))).thenReturn(newAttachment);

        Attachment createdAttachment = attachmentService.createAttachment(newAttachment);

        assertEquals("some-url", createdAttachment.getUrl());
        verify(attachmentRepository, times(1)).save(newAttachment);
    }

    @Test
    void testUpdateAttachment() {
        Attachment existingAttachment = new Attachment();
        UUID randomId = UUID.randomUUID();
        existingAttachment.setId(randomId);
        existingAttachment.setUrl("some-url");

        Attachment updatedAttachment = new Attachment();
        updatedAttachment.setUrl("new-url");

        when(attachmentRepository.findById(randomId)).thenReturn(Optional.of(existingAttachment));
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(updatedAttachment);

        Optional<Attachment> result = attachmentService.updateAttachment(randomId, updatedAttachment);

        assertTrue(result.isPresent());
        assertEquals("new-url", result.get().getUrl());
        verify(attachmentRepository, times(1)).findById(randomId);
        verify(attachmentRepository, times(1)).save(existingAttachment);
    }

    @Test
    void testDeleteAttachment() {
        UUID randomId = UUID.randomUUID();
        attachmentService.deleteAttachment(randomId);

        verify(attachmentRepository, times(1)).deleteById(randomId);
    }
}
