package kr.hhplus.be.server.infrastructure.external.kafka;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.infrastructure.external.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataPlatformConsumer {
    private final ReservationRepository reservationRepository;
    private final ExternalApiClient apiClient;

    // 예약 완료 토픽에 대해 구독
    @KafkaListener(topics = "completedReservation",groupId = "external")
    public void sendReservationInfo(String reservationId){
        Reservation reservation = reservationRepository.findById(Long.valueOf(reservationId)).orElseThrow();
        // 데이터플랫폼으로 예약정보 전송
        apiClient.sendReservationInfo(reservation.getId(),reservation.getUser().getId(),reservation.getConcertSeat().getId(),reservation.getPrice());
    }

    // 결제 완료 토픽에 대해 구독
    @KafkaListener(topics = "completedReservation",groupId = "external")
    public void sendPaymentInfo(String reservationId){
        Reservation reservation = reservationRepository.findById(Long.valueOf(reservationId)).orElseThrow();
        // 데이터플랫폼으로 예약정보 전송
        apiClient.sendPaymentInfo(reservation.getId(),reservation.getUser().getId(),reservation.getConcertSeat().getId(),reservation.getPrice());
    }
}
