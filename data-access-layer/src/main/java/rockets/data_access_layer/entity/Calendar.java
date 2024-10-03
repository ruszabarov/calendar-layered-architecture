package rockets.data_access_layer.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Size(min = 1, max = 2000, message = "title should be between 1 and 2000 characters")
    @Column(nullable = false)
    String title;

    @Size(max = 10000, message = "length of details should not exceed 10000 characters")
    String details;

    @Size(min = 1, message = "At least one meeting is required")
    @ManyToMany
    @JoinTable(
            name = "calendar_meeting",
            joinColumns = @JoinColumn(name = "calendar_id"),
            inverseJoinColumns = @JoinColumn(name = "meeting_id")
    )
    @JsonIgnoreProperties("calendars")
    Set<Meeting> meetings = new HashSet<>();

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

    public Set<Meeting> getMeetings() {
        return this.meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    public void addMeetings(List<Meeting> meetings) {
        this.meetings.addAll(meetings);
    }

    public void removeMeetings(List<Meeting> meetings) {
        meetings.forEach(this.meetings::remove);
    }
}
