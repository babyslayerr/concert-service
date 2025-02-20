package kr.hhplus.be.server.application.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.reservation.*;
import kr.hhplus.be.server.infrastructure.reservation.kafka.ReservationKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    private final ConcertService concertService;
    private final ReservationCreatedOutboxRepository reservationCreatedOutboxRepository;
    private final ReservationKafkaProducer reservationKafkaProducer;
    private final PaymentCreatedOutboxRepository paymentCreatedOutboxRepository;

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

    @Scheduled(fixedRate = 10000)
    void rePublishReservationCompletion(){
        // waitMinute - n 분동안 Pending 인 경우의 n
        Long waitMinute = 5L;
        reservationService.rePublishReservationCompletion(waitMinute);
    }

    @Scheduled(fixedRate = 10000)
    void rePublishPaymentCompletion(){
        Long waitMinute = 5L;
        reservationService.rePublishPaymentCompletion(waitMinute);

    }
}
