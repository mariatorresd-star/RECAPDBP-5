package Flight.Domain;

import Booking.Domain.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "flights", indexes = {
        @Index(name = "idx_flights_flight_number", columnList = "flightNumber"),
        @Index(name = "idx_flights_airline", columnList = "airline"),
        @Index(name = "idx_flights_departure_time", columnList = "departureTime")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_flights_flight_number", columnNames = "flightNumber")
})
public class Flight {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{1,6}$",
            message = "flightNumber debe ser A-Z/0-9, máx 6")
    @Column(nullable = false, length = 6)
    private String flightNumber;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String airlineName;

    @NotBlank @Column(nullable = false, length = 3)
    private String originIata;      // ej: LIM

    @NotBlank @Column(nullable = false, length = 3)
    private String destinationIata; // ej: MIA

    @NotNull @Column(nullable = false)
    private OffsetDateTime EstDepartureTime;

    @NotNull @Column(nullable = false)
    private OffsetDateTime EstArrivalTime;

    @NotNull @Positive
    @Column(nullable = false)
    private Integer seatCapacity;

    @NotNull @Positive
    @Column(nullable = false)
    private Integer Availableseats;

}
