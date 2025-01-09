package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.any;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void 콘서트스케줄_유저아이디가_좌석이_주어지면_해당좌석을_예약한다(){
        // given
        Long seatNo = 1L; // 좌석번호 1번
        Long concertScheduleId = 1L; // 1번 콘서트
        Long userId = 1L; // 1번 유저

        // 첫번째 인자로 받은 객체를 반환
        BDDMockito.given(reservationRepository.save(any(Reservation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        Reservation savedReservation = reservationService.makeReservation(seatNo, concertScheduleId, userId);

        // then
        Assertions.assertEquals("reserved", savedReservation.getStatus());
    }

    @Test
    void 결제전_예약이_만료시_예외가_발생한다(){

        // given
        Long reservationId = 1L;
        Reservation mockReservation = Reservation.builder()
                .id(reservationId)
                .status("expired")
                .build();
        BDDMockito.given(reservationRepository.findById(reservationId))
                .willReturn(Optional.of(mockReservation));

        // when / then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reservationService.completeReservation(reservationId);
        });

    }

}
