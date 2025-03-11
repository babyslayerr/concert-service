package kr.hhplus.be.server.application.reservation.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.reservation.OutboxStatus;
import kr.hhplus.be.server.domain.reservation.ReservationOutbox;

import java.time.LocalDateTime;



public record CompletedPaymentEvent(Long reservationId, Long userId, Long seatId, Long price) {
    private static final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    public static ReservationOutbox toOutboxEntity(CompletedPaymentEvent event) {
        String payloadJson = convertToJson(event); // JSON 변환

        return ReservationOutbox.builder()
                .reservationId(event.reservationId())
                .status(OutboxStatus.PENDING)
                .createdDateTime(LocalDateTime.now())
                .eventName("CompletedPaymentEvent")
                .payload(payloadJson) // JSON 저장
                .build();
    }

    private static String convertToJson(CompletedPaymentEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}
