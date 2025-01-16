package kr.hhplus.be.server.unit.domain;

import kr.hhplus.be.server.domain.reservation.Reservation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    @Test
    void 예약완료시_상태가_완료상태가_된다(){
        // given
        Reservation reservation = Reservation.builder().status("reserved").build();

        // when
        reservation.completeReservation();

        // then
        assertEquals("completed",reservation.getStatus());
        assertNotNull(reservation.getStatusUpdateAt());

    }
    @Test
    void 완료처리전_만료시_예외가_발생한다(){
        // given
        Reservation reservation = Reservation.builder().status("expired").build();

        // when, then
        assertThrows(IllegalStateException.class,()->{
            reservation.completeReservation();
        });
    }

    @Test
    void 예약이_만료상태가_된다(){
        // given
        Reservation reservation = Reservation.builder().build();

        // when
        reservation.expire();

        // then
        assertEquals("expired",reservation.getStatus());
        assertNotNull(reservation.getStatusUpdateAt());
    }
}
