package kr.hhplus.be.server.unit.domain;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcertSeatTest {
    @Test
    void 콘서트좌석은_예약시_예약상태로_변경된다(){

        // given
        // 예약자
        User user = new User();
        // 콘서트 좌석 생성
        ConcertSeat availableSeat = ConcertSeat.builder()
                .status("available")
                .build();

        // when
        availableSeat.reserved(user);

        assertEquals("reserved",availableSeat.getStatus());
        assertEquals(user,availableSeat.getUser());


    }

    @Test
    void 콘서트좌석은_결제완료시_완료로_상태가_변경된다(){
        // given
        ConcertSeat reservedSeat = ConcertSeat.builder()
                .status("reserved")
                .build();

        // when
        reservedSeat.setCompletedStatus();

        // then
        assertEquals("completed",reservedSeat.getStatus());
    }

    @Test
    void 콘서트좌석이_예약상태가아니면_에러가반환된다(){

        // given
        ConcertSeat reservedSeat = ConcertSeat.builder()
                .status("available")
                .build();
        // when, then
        assertThrows(IllegalStateException.class,()->
                reservedSeat.setCompletedStatus());

    }

    @Test
    void 콘서트좌석이_가능상태로_변경된다(){
        // given
        ConcertSeat reservedSeat = ConcertSeat.builder()
                .status("reserved")
                .build();
        // when
        reservedSeat.setAvailableStatus();

        // then
        assertEquals("available",reservedSeat.getStatus());
    }
}
