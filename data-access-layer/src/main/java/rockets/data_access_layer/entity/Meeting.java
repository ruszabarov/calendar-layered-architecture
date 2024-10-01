package rockets.data_access_layer.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title;
    Date dateTime;
    String location;
    String details;

    @ManyToMany(mappedBy = "meetings")
    Set<Calendar> calendars = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "meeting_participant",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    Set<Participant> participants = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "meeting_attachment",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    Set<Attachment> attachments = new HashSet<>();

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

    public Set<Participant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipants(List<Participant> participants) {
        this.participants.addAll(participants);
    }

    public void removeParticipants(List<Participant> participants) {
        participants.forEach(this.participants::remove);
    }

    public void addAttachments(List<Attachment> attachments) {
        this.attachments.addAll(attachments);
    }

    public void removeAttachments(List<Attachment> attachments) {
        attachments.forEach(this.attachments::remove);
    }

    public Set<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(Set<Calendar> calendars) {
        this.calendars = calendars;
    }
}
