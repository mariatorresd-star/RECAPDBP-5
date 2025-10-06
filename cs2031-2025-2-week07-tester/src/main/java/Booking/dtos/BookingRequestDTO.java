package Booking.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingRequestDTO {
    private Long customerId;
    private Long flightId;
    private Integer seats;
}
