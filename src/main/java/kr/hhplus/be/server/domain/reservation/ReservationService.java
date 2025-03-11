package kr.hhplus.be.server.domain.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.reservation.kafka.ReservationKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationOutboxRepository reservationOutboxRepository;
    private final ReservationKafkaProducer reservationKafkaProducer;

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    // 예약 생성
    // facade 에 트랜잭션 걸려있음
    public Reservation makeReservation(User user, ConcertSeat concertSeat, Long price) {

        Reservation reservation = Reservation.builder()
                .user(user)
                .concertSeat(concertSeat)
                .price(price)
                .status("reserved")
                .statusUpdateAt(LocalDateTime.now())
                .statusUpdateAt(LocalDateTime.now().plusMinutes(5)) // 만료시간은 생성시점+5분까지
                .build();
        // 저장
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }

    // 예약 완료(결제 완료 상태)
    public void completeReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        if(reservation.getStatus().equals("expired")){
            log.warn("reservation status already expired");
            throw new IllegalStateException("만료된 예약입니다.");
        }
        reservation.completeReservation();
        log.info("Reservation Status: {}",reservation.getStatus());
        reservationRepository.flush();
    }

    // 예약만료 스케줄러에서 사용하는 예약만료
    public List<Reservation> expireReservation() {
        // 만료시간이 현재 시간보다 뒤에 있으면 만료 처리 (만료시간 < 현재시간) -> 만료
        List<Reservation> reservationList = reservationRepository.findByExpireAtBeforeAndStatus(LocalDateTime.now(),"reserved");
        log.info("{} reservations will expire ",reservationList.size());
        reservationList.forEach(reservation -> {
            reservation.expire();
            reservationRepository.save(reservation);
        });
        return reservationList;
    }

    public void rePublishReservationCompletion(Long waitMinute) {
        reservationOutboxRepository.findAllByStatusAndEventName(OutboxStatus.PENDING,"CompletedReservationEvent")
                .stream()
                .forEach((e)->{
                    // n 분동안 상태가 Pending 인 경우 메세지 재발행
                    if(e.getCreatedDateTime().minusMinutes(waitMinute).isAfter(LocalDateTime.now())){
                        // 메세지 재발행
                        Reservation reservation = reservationRepository.findById(e.getReservationId()).orElseThrow();
                        reservationKafkaProducer.publishCompletedReservation(reservation);
                        // 재발송 방지를 위한 기존 메세지 각각 삭제
                        reservationOutboxRepository.delete(e);
                    }
                });
    }

    public void rePublishPaymentCompletion(Long waitMinute) {
        reservationOutboxRepository.findAllByStatusAndEventName(OutboxStatus.PENDING,"CompletedPaymentEvent")
                .stream()
                .forEach((e)->{
                    // 5분동안 상태가 Pending 인 경우 메세지 재발행
                    if(e.getCreatedDateTime().minusMinutes(waitMinute).isAfter(LocalDateTime.now())){
                        // 메세지 재발행
                        Reservation reservation = reservationRepository.findById(e.getReservationId()).orElseThrow();
                        reservationKafkaProducer.publishCompletedReservation(reservation);
                        // 재발송 방지를 위한 기존 메세지 각각 삭제
                        reservationOutboxRepository.delete(e);
                    }
                });
    }
}
