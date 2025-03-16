package ch.mycrypto.cryptowalletapi.infrastructure.user.persistence;

import ch.mycrypto.cryptowalletapi.domain.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    public UserEntity() {
    }

    public UserEntity(
            UUID id,
            String email
    ) {
        this.id = id;
        this.email = email;
    }

    public static UserEntity with(final User aUser) {
        return new UserEntity(
                UUID.fromString(aUser.id()),
                aUser.email()
        );
    }

    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
