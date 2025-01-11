package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    @Scheduled(fixedRate = 10000)
    @Transactional
    void expireReservation(){
        // 예약 예약만료 처리
        List<Reservation> expiredReservationList = reservationService.expireReservation(); // 조회된 리스트는 영속상태

        // 예약과 매핑된 좌석을 예약 가능한 상태로 변경
        expiredReservationList.forEach(reservation -> {
            reservation.getConcertSeat().setReservedStatus(); // 트랜잭션 종료시점에 반영
        });
    }
}
