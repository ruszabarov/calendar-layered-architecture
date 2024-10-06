package rockets.data_access_layer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Size(min = 1, max = 600, message = "name should be between 1 and 600 characters")
    @Column(nullable = false)
    String name;

    @Email(message = "email should be valid")
    String email;

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    Set<Meeting> meetings = new HashSet<>();

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }
}
