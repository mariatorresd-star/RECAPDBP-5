package Flight.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightBookingRequestDTO {
    @NotNull
    private Long flightId;
}
