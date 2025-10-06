package Flight.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class FlightBookingResponseDTO {
    private Long id;
    private OffsetDateTime bookingDate;
    private Long flightId;
    private String flightNumber;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;

}
