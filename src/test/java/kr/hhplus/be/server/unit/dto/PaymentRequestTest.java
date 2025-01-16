package kr.hhplus.be.server.unit.dto;

import kr.hhplus.be.server.presentation.reservation.dto.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class PaymentRequestTest {
    @Test
    void 요청시_userId가누락되면_예외발생(){
        Assertions.assertThrows(RuntimeException.class,()->{
            new PaymentRequest(null, 1L, 1L);
        });

    }
    @Test
    void 요청시_concertScheduleId가누락되면_예외발생(){
        Assertions.assertThrows(RuntimeException.class,()->{
            new PaymentRequest(1L, null, 1L);
        });

    }
    @Test
    void 요청시_reservationId가누락되면_예외발생(){

        Assertions.assertThrows(RuntimeException.class,()->{
            new PaymentRequest(1L, 1L, null);
        });
    }
}
