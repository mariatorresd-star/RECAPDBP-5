package Flight.infraestructure;

import Flight.Domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByDepartureTimeBetween(OffsetDateTime from, OffsetDateTime to);
}