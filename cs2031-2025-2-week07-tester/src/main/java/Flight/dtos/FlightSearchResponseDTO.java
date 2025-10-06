package Flight.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class FlightSearchResponseDTO {
    private Long id;
    private String airlineName;
    private String flightNumber;
    private OffsetDateTime estDepartureTime;
    private OffsetDateTime estArrivalTime;
    private Integer availableSeats;
}
