package User.domain;


import User.Infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getEmail() != null) user.setEmail(user.getEmail().trim().toLowerCase());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("User with this email already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User changes) {
        User current = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (changes.getEmail() != null) {
            String newEmail = changes.getEmail().trim().toLowerCase();
            if (!newEmail.equals(current.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new IllegalStateException("Email already in use");
            }
            current.setEmail(newEmail);
        }
        if (changes.getFirstName() != null) current.setFirstName(changes.getFirstName());
        if (changes.getLastName()  != null) current.setLastName(changes.getLastName());
        if (changes.getPassword()  != null) current.setPassword(changes.getPassword());
        if (changes.getRole()      != null) current.setRole(changes.getRole());
        return userRepository.save(current);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) throw new NoSuchElementException();
        userRepository.deleteById(id);
    }
}