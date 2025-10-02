package Booking.Domain;


import Booking.Infraestructure.BookingRepository;
import Flight.Domain.Flight;
import Flight.infraestructure.FlightRepository;
import User.Infraestructure.UserRepository;
import User.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Booking> listBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking b) {
        if (b.getCustomer() == null || b.getCustomer().getId() == null)
            throw new IllegalArgumentException("customer is required");
        if (b.getFlight() == null || b.getFlight().getId() == null)
            throw new IllegalArgumentException("flight is required");

        User customer = userRepository.findById(b.getCustomer().getId())
                .orElseThrow(NoSuchElementException::new);
        Flight flight = flightRepository.findById(b.getFlight().getId())
                .orElseThrow(NoSuchElementException::new);

        if (flight.getEstDepartureTime() != null && !OffsetDateTime.now().isBefore(flight.getEstDepartureTime())) {
            throw new IllegalStateException("cannot book a past/in-progress flight");
        }
        if (flight.getAvailableseats() == null || flight.getAvailableseats() <= 0) {
            throw new IllegalStateException("no seats available");
        }
        flight.setAvailableseats(flight.getAvailableseats() - 1);

        b.setCustomer(customer);
        b.setFlight(flight);
        if (b.getBookedAt() == null) b.setBookedAt(OffsetDateTime.now());
        if (b.getStatus()  == null)  b.setStatus(status.BOOKED);
        if (b.getCustomerFullName() == null) {
            String fn = customer.getFirstName() != null ? customer.getFirstName() : "";
            String ln = customer.getLastName()  != null ? customer.getLastName()  : "";
            b.setCustomerFullName((fn + " " + ln).trim());
        }
        if (b.getFlightNumber() == null) {
            b.setFlightNumber(flight.getFlightNumber());
        }

        flightRepository.save(flight);
        return bookingRepository.save(b);
    }

    public Booking updateBooking(Long id, Booking changes) {
        Booking current = bookingRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (changes.getStatus() != null) current.setStatus(changes.getStatus());
        if (changes.getSeats()  != null) current.setSeats(changes.getSeats());
        return bookingRepository.save(current);
    }

    public void deleteBookingById(Long id) {
        if (!bookingRepository.existsById(id)) throw new NoSuchElementException();
        bookingRepository.deleteById(id);
    }

    public List<Booking> listBookingsByCustomer(Long userId) {
        return bookingRepository.findByCustomerId(userId);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking b = bookingRepository.findById(bookingId).orElseThrow(NoSuchElementException::new);
        if (b.getStatus() == status.CANCELLED) return b;
        b.setStatus(status.CANCELLED);

        Flight f = b.getFlight();
        if (f != null && f.getAvailableseats() != null) {
            f.setAvailableseats(f.getAvailableseats() + 1);
            flightRepository.save(f);
        }
        return bookingRepository.save(b);
    }
}