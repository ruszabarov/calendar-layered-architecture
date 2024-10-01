package rockets.data_access_layer.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    @Column(nullable = false)
    String title;
    String details;

    @ManyToMany
    @JoinTable(
            name = "calendar_meeting",
            joinColumns = @JoinColumn(name = "calendar_id"),
            inverseJoinColumns = @JoinColumn(name = "meeting_id")
    )
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
