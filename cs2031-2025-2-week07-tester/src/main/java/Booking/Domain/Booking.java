package Booking.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

import User.domain.User;
import Flight.Domain.Flight;

@Getter
@Setter
@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_bookings_customer_id", columnList = "customer_id"),
        @Index(name = "idx_bookings_flight_id", columnList = "flight_id")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_user"))
    private User customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_flight"))
    private Flight flight;

    @Column(nullable = false, length = 120)
    private String customerFullName;

    @Column(nullable = false, length = 6)
    private String flightNumber;

    @Column(nullable = false)
    private OffsetDateTime bookedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private status status = Booking.Domain.status.BOOKED;

    @NotNull
    @Positive
    @Max(1)
    @Column(nullable = false)
    private Integer seats = 1;

    @PrePersist
    private void prePersist() {
        if (bookedAt == null) {
            bookedAt = OffsetDateTime.now();
        }
        if (customerFullName == null && customer != null) {
            String fn = customer.getFirstName() != null ? customer.getFirstName() : "";
            String ln = customer.getLastName()  != null ? customer.getLastName()  : "";
            customerFullName = (fn + " " + ln).trim();
        }
        if (flightNumber == null && flight != null) {
            flightNumber = flight.getFlightNumber();
        }
    }
}
