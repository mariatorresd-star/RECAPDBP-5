package Booking.Infraestructure;


import Booking.Domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long userId);

    boolean existsByCustomerIdAndFlightId(Long userId, Long flightId);
}