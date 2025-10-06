package Email.application;

import Email.domain.EmailService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Validated
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/booking-confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void sendBookingConfirmation(@RequestBody @Validated BookingEmailRequest req) throws Exception {
        emailService.sendBookingConfirmation(
                req.to(),
                req.bookingId(),
                req.passengerNames(),
                req.flightNumber(),
                req.departure(),
                req.arrival()
        );
    }

    public record BookingEmailRequest(
            @NotBlank @Email String to,
            @NotBlank String bookingId,
            @NotBlank String passengerNames,
            @NotBlank String flightNumber,
            @NotNull OffsetDateTime departure,
            @NotNull OffsetDateTime arrival
    ) {}
}
