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
@Table(name = "reservation_outbox")
public class ReservationOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private Long reservationId;
    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    // 이벤트 객체 명
    private String eventName;

    // 이벤트 객체의 json
    @Column(columnDefinition = "LONGTEXT")
    private String payload;
    public void published() {
        this.status = OutboxStatus.PUBLISHED;
    }
}
