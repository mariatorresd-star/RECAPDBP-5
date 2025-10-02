package Flight.Domain;


import Flight.infraestructure.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Flight> listFlights() {
        return flightRepository.findAll();
    }

    public Flight createFlight(Flight f) {
        validate(f);
        normalizeIata(f);
        return flightRepository.save(f);
    }

    public Flight updateFlight(Long id, Flight changes) {
        Flight current = flightRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (changes.getFlightNumber()   != null) current.setFlightNumber(changes.getFlightNumber());
        if (changes.getAirlineName()        != null) current.setAirlineName(changes.getAirlineName());
        if (changes.getOriginIata()     != null) current.setOriginIata(changes.getOriginIata());
        if (changes.getDestinationIata()!= null) current.setDestinationIata(changes.getDestinationIata());
        if (changes.getEstDepartureTime()  != null) current.setEstDepartureTime(changes.getEstDepartureTime());
        if (changes.getEstArrivalTime()    != null) current.setEstArrivalTime(changes.getEstArrivalTime());
        if (changes.getSeatCapacity()   != null) current.setSeatCapacity(changes.getSeatCapacity());
        if (changes.getAvailableseats() != null) current.setAvailableseats(changes.getAvailableseats());

        validate(current);
        normalizeIata(current);
        return flightRepository.save(current);
    }

    public void deleteFlightById(Long id) {
        if (!flightRepository.existsById(id)) throw new NoSuchElementException();
        flightRepository.deleteById(id);
    }

    public List<Flight> listByDepartureBetween(OffsetDateTime from, OffsetDateTime to) {
        return flightRepository.findByDepartureTimeBetween(from, to);
    }

    private void validate(Flight f) {
        if (f.getEstDepartureTime() != null && f.getEstArrivalTime() != null
                && !f.getEstDepartureTime().isBefore(f.getEstArrivalTime())) {
            throw new IllegalArgumentException("Departure must be before arrival");
        }
        if (f.getSeatCapacity() != null && f.getAvailableseats() != null
                && f.getAvailableseats() > f.getSeatCapacity()) {
            throw new IllegalArgumentException("Seats available cannot exceed capacity");
        }
        if (f.getOriginIata() != null && f.getDestinationIata() != null
                && f.getOriginIata().equalsIgnoreCase(f.getDestinationIata())) {
            throw new IllegalArgumentException("Origin and destination cannot be the same");
        }
    }

    private void normalizeIata(Flight f) {
        if (f.getOriginIata() != null)      f.setOriginIata(f.getOriginIata().toUpperCase());
        if (f.getDestinationIata() != null) f.setDestinationIata(f.getDestinationIata().toUpperCase());
    }
}