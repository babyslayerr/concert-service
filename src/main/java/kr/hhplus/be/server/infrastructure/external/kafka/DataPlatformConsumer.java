package kr.hhplus.be.server.infrastructure.external.kafka;

import kr.hhplus.be.server.domain.reservation.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataPlatformConsumer { // 외부 데이터 플랫폼 Consumer, 별도 서버 구축 예정

    // 예약 완료 토픽에 대해 구독
    @KafkaListener(topics = "completedReservation",groupId = "external")
    public void sendReservationInfo(ReservationDto reservationDto){
        System.out.printf("데이터플랫폼의 예약완료 topic 구독 완료 - 예약 ID: %d, 유저 ID: %d, 좌석 ID: %d, 가격: %d%n",
                reservationDto.getId(), reservationDto.getUserId(), reservationDto.getConcertSeatId() ,reservationDto.getPrice());
    }

    // 결제 완료 토픽에 대해 구독
    @KafkaListener(topics = "completedPayment",groupId = "external")
    public void sendPaymentInfo(ReservationDto reservationDto){
        System.out.printf("데이터플랫폼의 결제완료 topic 구독 완료  - 예약 ID: %d, 유저 ID: %d, 좌석 ID: %d, 가격: %d%n",
                reservationDto.getId(), reservationDto.getUserId(), reservationDto.getConcertSeatId() ,reservationDto.getPrice());
    }
}
