package rockets.data_access_layer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rockets.data_access_layer.entity.Calendar;

import java.util.UUID;

public interface CalendarRepository extends JpaRepository<Calendar, UUID> {

}
