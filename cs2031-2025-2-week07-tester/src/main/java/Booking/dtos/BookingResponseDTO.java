package Booking.dtos;

import Booking.Domain.status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private String customerFullName;
    private String flightNumber;
    private OffsetDateTime bookedAt;
    private status status;
    private Integer seats;
}
