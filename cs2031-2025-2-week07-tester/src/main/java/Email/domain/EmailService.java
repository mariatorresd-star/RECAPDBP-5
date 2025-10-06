package Email.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendBookingConfirmation(String to, String bookingId, String passengerNames,
                                        String flightNumber, OffsetDateTime departure, OffsetDateTime arrival) throws Exception {

        DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String body = """
                Hola %s,

                ¡Tu reserva ha sido confirmada!

                • Booking ID: %s
                • Pasajero(s): %s
                • Vuelo: %s
                • Salida: %s
                • Llegada: %s
                """.formatted(
                passengerNames,
                bookingId,
                passengerNames,
                flightNumber,
                departure.format(ISO),
                arrival.format(ISO)
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de vuelo " + flightNumber);
        message.setText(body);
        mailSender.send(message);

        Path file = Paths.get("flight_booking_email_" + bookingId + ".txt");
        Files.writeString(file, body, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
