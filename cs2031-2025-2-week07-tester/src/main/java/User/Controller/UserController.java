package User.Controller;

import User.domain.User;
import User.domain.UserService;
import User.dtos.NewIdDTO;
import User.dtos.RegisterUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // UNPROTECTED
    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@RequestBody RegisterUserDTO newUserDTO) {
        try {
            // Validaciones básicas (según enunciado)
            if (newUserDTO.getFirstName() == null || newUserDTO.getLastName() == null ||
                    newUserDTO.getEmail() == null || newUserDTO.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Crear objeto de dominio
            User user = new User();
            user.setFirstName(newUserDTO.getFirstName());
            user.setLastName(newUserDTO.getLastName());
            user.setEmail(newUserDTO.getEmail());
            user.setPassword(newUserDTO.getPassword());

            // Guardar y devolver id
            User saved = userService.createUser(user);
            NewIdDTO response = new NewIdDTO(saved.getId().toString());
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            // Email duplicado u otro problema de negocio
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
