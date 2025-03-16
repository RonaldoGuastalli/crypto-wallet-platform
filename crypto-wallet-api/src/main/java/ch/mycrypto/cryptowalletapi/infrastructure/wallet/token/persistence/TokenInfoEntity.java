package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfoEntity {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(name = "asset_id", nullable = false, unique = true)
    private String assetId;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
