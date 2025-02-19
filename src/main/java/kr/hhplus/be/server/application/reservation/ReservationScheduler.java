package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
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

    private final ConcertService concertService;

    @Scheduled(fixedRate = 10000)
    @Transactional
    void expireReservation(){
        // 예약 예약만료 처리
        List<Reservation> reservationList = reservationService.expireReservation();// 조회된 리스트는 영속상태

        // 예약에 해당하는 좌석 원복 처리
        List<ConcertSeat> concertSeats = reservationList.stream().map((reservation -> {
            return reservation.getConcertSeat();
        })).toList();
        concertService.makeSeatsAvailable(concertSeats);

    }
}
