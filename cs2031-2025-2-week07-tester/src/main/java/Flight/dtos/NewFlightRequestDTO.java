package Flight.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
public class NewFlightRequestDTO {
    @NotBlank
    private String airlineName;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "flightNumber debe ser A-Z/0-9, máx 6")
    private String flightNumber;

    @NotNull
    private OffsetDateTime estDepartureTime;

    @NotNull
    private OffsetDateTime estArrivalTime;

    @NotNull
    @Positive
    private Integer availableSeats;
}
