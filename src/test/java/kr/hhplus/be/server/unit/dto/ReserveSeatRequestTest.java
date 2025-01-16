package kr.hhplus.be.server.unit.dto;


import kr.hhplus.be.server.presentation.reservation.dto.PaymentRequest;
import kr.hhplus.be.server.presentation.reservation.dto.ReserveSeatRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReserveSeatRequestTest {
    @Test
    void 요청시_userId가누락되면_예외발생() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new ReserveSeatRequest(null, 1L, 1L);
        });

    }

    @Test
    void 요청시_concertScheduleId가누락되면_예외발생() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new ReserveSeatRequest(1L, null, 1L);
        });

    }

    @Test
    void 요청시_reservationId가누락되면_예외발생() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            new ReserveSeatRequest(1L, 1L, null);
        });
    }
}