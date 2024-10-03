package rockets.data_access_layer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public class CalendarDTO {
    private UUID id;

    @Size(min = 1, max = 2000, message = "Title should be between 1 and 2000 characters")
    @NotNull(message = "Title is required")
    private String title;

    @Size(max = 10000, message = "Length of details should not exceed 10000 characters")
    private String details;

    private Set<UUID> meetingIds;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<UUID> getMeetingIds() {
        return this.meetingIds;
    }

    public void setMeetings(Set<UUID> meetingIds) {
        this.meetingIds = meetingIds;
    }

    public void addMeetings(Set<UUID> meetingIds) {
        this.meetingIds.addAll(meetingIds);
    }

    public void removeMeetings(Set<UUID> meetingIds) {
        meetingIds.forEach(this.meetingIds::remove);
    }
}

