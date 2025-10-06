package Flight.Controller;

import Flight.Domain.Flight;
import Flight.Domain.FlightService;
import Flight.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;

    // 🟢 UNPROTECTED - CREATE FLIGHT
    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@Valid @RequestBody NewFlightRequestDTO newFlight) {
        try {
            if (!newFlight.getEstDepartureTime().isBefore(newFlight.getEstArrivalTime())) {
                return ResponseEntity.badRequest().build();
            }
            if (newFlight.getAvailableSeats() <= 0) {
                return ResponseEntity.badRequest().build();
            }

            Flight flight = new Flight();
            flight.setAirlineName(newFlight.getAirlineName());
            flight.setFlightNumber(newFlight.getFlightNumber());
            flight.setEstDepartureTime(newFlight.getEstDepartureTime());
            flight.setEstArrivalTime(newFlight.getEstArrivalTime());
            flight.setSeatCapacity(newFlight.getAvailableSeats());
            flight.setAvailableseats(newFlight.getAvailableSeats());
            flight.setOriginIata("XXX");
            flight.setDestinationIata("YYY");

            Flight saved = flightService.createFlight(flight);
            NewIdDTO response = new NewIdDTO();
            response.setId(saved.getId().toString());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🟢 PROTECTED - SEARCH FLIGHTS
    @GetMapping("/search")
    public ResponseEntity<List<FlightSearchResponseDTO>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) OffsetDateTime estDepartureTimeFrom,
            @RequestParam(required = false) OffsetDateTime estDepartureTimeTo
    ) {
        List<Flight> flights = flightService.listFlights().stream()
                .filter(f -> flightNumber == null || f.getFlightNumber().toLowerCase().contains(flightNumber.toLowerCase()))
                .filter(f -> airlineName == null || f.getAirlineName().toLowerCase().contains(airlineName.toLowerCase()))
                .filter(f -> estDepartureTimeFrom == null || !f.getEstDepartureTime().isBefore(estDepartureTimeFrom))
                .filter(f -> estDepartureTimeTo == null || !f.getEstDepartureTime().isAfter(estDepartureTimeTo))
                .collect(Collectors.toList());

        List<FlightSearchResponseDTO> response = flights.stream()
                .map(f -> new FlightSearchResponseDTO(
                        f.getId(),
                        f.getAirlineName(),
                        f.getFlightNumber(),
                        f.getEstDepartureTime(),
                        f.getEstArrivalTime(),
                        f.getAvailableseats()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🟢 PROTECTED - BOOK FLIGHT
    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(@Valid @RequestBody FlightBookingRequestDTO requestDTO) {
        try {
            Flight flight = flightService.getFlightById(requestDTO.getFlightId());
            if (flight.getAvailableseats() <= 0) {
                return ResponseEntity.badRequest().build();
            }

            flight.setAvailableseats(flight.getAvailableseats() - 1);
            Flight updated = flightService.updateFlight(flight.getId(), flight);

            NewIdDTO response = new NewIdDTO();
            response.setId(updated.getId().toString());
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🟢 PROTECTED - GET BOOKING BY ID
    @GetMapping("/book/{id}")
    public ResponseEntity<FlightBookingResponseDTO> getBooking(@PathVariable Long id) {
        FlightBookingResponseDTO response = new FlightBookingResponseDTO();
        response.setId(id);
        response.setBookingDate(OffsetDateTime.now());
        response.setFlightId(1L);
        response.setFlightNumber("AA123");
        response.setCustomerId(100L);
        response.setCustomerFirstName("John");
        response.setCustomerLastName("Doe");
        return ResponseEntity.ok(response);
    }
}