package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.reservation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final QueueService queueService;

    // 콘서트 좌석 예약
    @Transactional
    public ReservationResponse reserveSeat(Long userId, Long concertScheduleId, Long seatNo) {
        // 유저 조회
        User user = userService.findUserById(userId);
        // 예약가능한 좌석 예약
        ConcertSeat reserveSeat = concertService.reserveAvailableSeat(user, concertScheduleId, seatNo);
        // 예약 내역 생성
        Reservation reservation = reservationService.makeReservation(user, reserveSeat, reserveSeat.getPrice());

        // Response 생성
        ReservationResponse response = ReservationResponse.fromEntity(reservation);

        return response;
    }

    // 좌석 결제
    @Transactional
    public void makeSeatPayment(Long userId, Long concertSeatId, Long reservationId, String tokenUuid) {
        // 해당 좌석 조회
        ConcertSeat concertSeat = concertService.findConcertSeatById(concertSeatId);

        // 결제 잔액 확인 및 차감
        userService.makePayment(userId, concertSeat.getPrice());

        // 좌석상태 변경 요청
        concertService.changeConcertSeatCompleted(concertSeat);

        // 예약 상태 변경(reserved -> completed)
        reservationService.completeReservation(reservationId);

        // 해당 사용된 토큰 삭제
        queueService.removeToken(tokenUuid);
    }
}
