package Flight.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class NewIdDTO {
    private String id;

    public NewIdDTO() {}

    public NewIdDTO(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

}
