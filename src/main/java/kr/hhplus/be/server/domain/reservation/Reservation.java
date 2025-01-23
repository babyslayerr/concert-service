package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne // 1개의 좌석은 하나의 예약을 가질 수 있고, 예약의 수 또한 1개의 좌석을 가질 수 있다.(한번의 여러개를 결제하지 않을 것임)
    @JoinColumn(name = "concert_seat_id") // 2개 결제는 없거나 2번 API 가 호출 되도록 클라이언트에서 제어
    private ConcertSeat concertSeat;
    private Long price;
    // reserved,expired,cancel,completed
    private String status;
    private LocalDateTime statusUpdateAt;
    private LocalDateTime expireAt;

    @Version
    @Column(nullable = false)
    private int version;

    public void completeReservation() {
        if(!this.status.equals("reserved")) throw new IllegalStateException("예약이 예약된 상태여야 합니다.");
        this.status = "completed";
        this.statusUpdateAt = LocalDateTime.now();
    }

    // 만료 처리
    public void expire() {
        status = "expired";
        statusUpdateAt = LocalDateTime.now();
    }
}
