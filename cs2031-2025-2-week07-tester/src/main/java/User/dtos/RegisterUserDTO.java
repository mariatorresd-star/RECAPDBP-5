package User.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
