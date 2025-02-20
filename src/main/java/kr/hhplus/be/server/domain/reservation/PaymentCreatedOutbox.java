package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_created_outbox")
public class PaymentCreatedOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private Long reservationId;
    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    public void published() {
        this.status = OutboxStatus.PUBLISHED;
    }


}
