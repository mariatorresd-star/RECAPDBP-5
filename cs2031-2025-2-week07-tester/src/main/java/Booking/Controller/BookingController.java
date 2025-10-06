package Booking.Controller;

import Booking.Domain.Booking;
import Booking.Domain.BookingService;
import Booking.dtos.BookingRequestDTO;
import Booking.dtos.BookingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // ✅ Crear una reserva
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO dto) {
        try {
            Booking booking = new Booking();
            booking.setSeats(dto.getSeats());
            // Asociar usuario y vuelo usando solo IDs
            var user = new Booking.Domain.Booking();
            booking.setCustomer(new User.domain.User());
            booking.getCustomer().setId(dto.getCustomerId());
            booking.setFlight(new Flight.Domain.Flight());
            booking.getFlight().setId(dto.getFlightId());

            Booking saved = bookingService.createBooking(booking);
            return ResponseEntity.ok(toResponse(saved));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(toResponse(booking));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> listAllBookings() {
        List<BookingResponseDTO> list = bookingService.listBookings()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ✅ Cancelar una reserva
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        try {
            Booking cancelled = bookingService.cancelBooking(id);
            return ResponseEntity.ok(toResponse(cancelled));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Listar reservas de un cliente
    @GetMapping("/customer/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> listByCustomer(@PathVariable Long userId) {
        List<BookingResponseDTO> list = bookingService.listBookingsByCustomer(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ✅ Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBookingById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔄 Conversión de entidad → DTO
    private BookingResponseDTO toResponse(Booking b) {
        return new BookingResponseDTO(
                b.getId(),
                b.getCustomerFullName(),
                b.getFlightNumber(),
                b.getBookedAt(),
                b.getStatus(),
                b.getSeats()
        );
    }
}
