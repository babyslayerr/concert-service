package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reservation_created_outbox")
public class ReservationCreatedOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    public void published() {
        this.status = OutboxStatus.PUBLISHED;
    }


}
