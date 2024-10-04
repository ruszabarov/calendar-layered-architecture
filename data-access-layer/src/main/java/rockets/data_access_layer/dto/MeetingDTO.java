package rockets.data_access_layer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MeetingDTO {
    UUID id;

    @NotNull(message = "title can not be null")
    @Size(min = 1, max = 2000, message = "title should be between 1 and 2000 characters")
    String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @FutureOrPresent(message = "dateTime should be in the future or present")
    Date dateTime;

    String location;

    @Size(max = 10000, message = "length of details should not exceed 10000 characters")
    String details;

    private Set<UUID> participantIds;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<UUID> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<UUID> participantIds) {
        this.participantIds = participantIds;
    }
}
