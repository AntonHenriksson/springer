package se.jensen.anton.springer.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "friendship",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "friend_pair",
                        columnNames = {"sender_id", "receiver_id"}
                )
        }
)
public class Friendship {
    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;


    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt = Instant.now();

    private Instant respondedAt;

}
