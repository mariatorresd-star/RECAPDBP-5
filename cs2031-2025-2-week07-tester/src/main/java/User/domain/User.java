package User.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, length = 180)
    private String email;

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "firstName debe contener al menos una mayúscula")
    @Column(nullable = false, length = 80)
    private String firstName;

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "lastName debe contener al menos una mayúscula")
    @Column(nullable = false, length = 80)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
            message = "password debe tener ≥8, con letras y números")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.CUSTOMER;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Booking.Domain.Booking> bookings = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
